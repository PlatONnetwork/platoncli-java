package com.cicdi.jcli.template.government;

import com.alaya.contracts.ppos.dto.common.DuplicateSignType;
import lombok.Data;

/**
 * {
 * "type": 0,
 * "data": "",
 * "verify": ""
 * }
 *
 * @author haypo
 * @date 2021/1/17
 */
@Data
public class ReportDoubleSignTemplate {
    private DuplicateSignType type;
    private String data;
}
