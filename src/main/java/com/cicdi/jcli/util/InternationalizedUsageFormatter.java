package com.cicdi.jcli.util;

import com.beust.jcommander.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * @author haypo
 * @date 2021/4/19
 */
@Slf4j
public class InternationalizedUsageFormatter extends DefaultUsageFormatter {
    public InternationalizedUsageFormatter(JCommander commander) {
        super(commander);
        this.commander = commander;
    }

    public final JCommander commander;

    @Override
    public void appendCommands(StringBuilder out, int indentCount, int descriptionIndent, String indent) {
        out.append(indent).append("  ").append(ResourceBundleUtil.getTextString("commands")).append(":\n");

        // The magic value 3 is the number of spaces between the name of the option and its description
        for (Map.Entry<JCommander.ProgramName, JCommander> commands : commander.getRawCommands().entrySet()) {
            Object arg = commands.getValue().getObjects().get(0);
            Parameters p = arg.getClass().getAnnotation(Parameters.class);

            if (p == null || !p.hidden()) {
                JCommander.ProgramName progName = commands.getKey();
                String dispName = progName.getDisplayName();
                String description = indent + s(4) + dispName + s(6) + getCommandDescription(progName.getName());
                wrapDescription(out, indentCount + descriptionIndent, description);
                out.append("\n");

                // Options for this command
                JCommander jc = commander.findCommandByAlias(progName.getName());
                InternationalizedUsageFormatter internationalizedUsageFormatter = new InternationalizedUsageFormatter(jc);
                internationalizedUsageFormatter.usage(out, indent + s(6));
                out.append("\n");
            }
        }
    }

    @Override
    public void appendMainLine(StringBuilder out, boolean hasOptions, boolean hasCommands, int indentCount,
                               String indent) {
        String programName = commander.getProgramDisplayName() != null
                ? commander.getProgramDisplayName() : "<main class>";
        StringBuilder mainLine = new StringBuilder();
        mainLine.append(indent).append(ResourceBundleUtil.getTextString("usage")).append(": ").append(programName);

        if (hasOptions) {
            mainLine.append(" [").append(ResourceBundleUtil.getTextString("options")).append("]");
        }

        if (hasCommands) {
            mainLine.append(indent).append(" [").append(ResourceBundleUtil.getTextString("command"))
                    .append("] [").append(ResourceBundleUtil.getTextString("commandOptions")).append("]");
        }
        Object mp = commander.getMainParameter();
        try {
            if (mp != null) {
                Method method = getDescriptionMethod(mp);
                ParameterDescription desc = (ParameterDescription) method.invoke(mp);
                if (commander.getMainParameter() != null && desc != null) {
                    mainLine.append(" ").append(desc.getDescription());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        wrapDescription(out, indentCount, mainLine.toString());
        out.append("\n");
    }

    private static String newLineAndIndent(int indent) {
        return "\n" + s(indent);
    }

    @Override
    public void appendAllParametersDetails(StringBuilder out, int indentCount, String indent,
                                           List<ParameterDescription> sortedParameters) {
        if (sortedParameters.size() > 0) {
            out.append(indent).append("  ").append(ResourceBundleUtil.getTextString("Options")).append(":\n");
        }

        for (ParameterDescription pd : sortedParameters) {
            WrappedParameter parameter = pd.getParameter();
            String description = pd.getDescription();
            boolean hasDescription = !description.isEmpty();

            // First line, command name
            out.append(indent)
                    .append("  ")
                    .append(parameter.required() ? "* " : "  ")
                    .append(pd.getNames())
                    .append("\n");

            if (hasDescription) {
                wrapDescription(out, indentCount, s(indentCount) + description);
            }
            Object def = pd.getDefault();

            if (pd.isDynamicParameter()) {
                String syntax = "Syntax: " + parameter.names()[0] + "key" + parameter.getAssignment() + "value";

                if (hasDescription) {
                    out.append(newLineAndIndent(indentCount));
                } else {
                    out.append(s(indentCount));
                }
                out.append(syntax);
            }

            if (def != null && !pd.isHelp()) {
                String displayedDef = Strings.isStringEmpty(def.toString()) ?
                        "<" + ResourceBundleUtil.getTextString("emptyString") + ">" : def.toString();
                String defaultText = ResourceBundleUtil.getTextString("Default") + ": " + (parameter.password() ? "********" : displayedDef);

                if (hasDescription) {
                    out.append(newLineAndIndent(indentCount));
                } else {
                    out.append(s(indentCount));
                }
                out.append(defaultText);
            }
            Class<?> type = pd.getParameterized().getType();

            if (type.isEnum()) {
                String valueList = EnumSet.allOf((Class<? extends Enum>) type).toString();
                String possibleValues = ResourceBundleUtil.getTextString("PossibleValues") + ": " + valueList;

                // Prevent duplicate values list, since it is set as 'Options: [values]' if the description
                // of an enum field is empty in ParameterDescription#init(..)
                if (!description.contains("Options: " + valueList)) {
                    if (hasDescription) {
                        out.append(newLineAndIndent(indentCount));
                    } else {
                        out.append(s(indentCount));
                    }
                    out.append(possibleValues);
                }
            }
            out.append("\n");
        }
    }

    public Method getDescriptionMethod(Object o) {
        Method[] methods = o.getClass().getMethods();
        for (Method method : methods) {
            if ("getDescription".equals(method.getName())) {
                return method;
            }
        }
        return null;
    }
}
