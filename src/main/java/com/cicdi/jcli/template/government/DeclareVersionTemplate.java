package com.cicdi.jcli.template.government;

import lombok.Data;

/**
 * @author haypo
 * @date 2020/12/31
 */
@Data
public class DeclareVersionTemplate {
    /**
     * 声明的节点id，只能是验证人/候选人。不支持输入中文，只能输入数字和英文字母，不区分大小写，最长不得超过128位
     */
    private String activeNode;
}
