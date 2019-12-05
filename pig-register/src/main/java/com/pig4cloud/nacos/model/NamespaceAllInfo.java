
package com.pig4cloud.nacos.model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * all namespace info
 *
 * @author Nacos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class NamespaceAllInfo extends Namespace {

    private String namespaceDesc;

    public NamespaceAllInfo(String namespace, String namespaceShowName, int quota, int configCount, int type,
                            String namespaceDesc) {
        super(namespace, namespaceShowName, quota, configCount, type);
        this.namespaceDesc = namespaceDesc;
    }

}
