
package com.pig4cloud.nacos.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Namespace
 *
 * @author diamond
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
public class Namespace implements Serializable {

    private static final long serialVersionUID = -4229714224025820103L;

    private String namespace;

    private String namespaceShowName;

    private int quota;

    private int configCount;
    /**
     * 0 : Global configuration， 1 : Default private namespace ，2 : Custom namespace
     */
    private int type;

    public Namespace(String namespace, String namespaceShowName) {
        this.namespace = namespace;
        this.namespaceShowName = namespaceShowName;
    }

    public Namespace(String namespace, String namespaceShowName, int quota, int configCount, int type) {
        this.namespace = namespace;
        this.namespaceShowName = namespaceShowName;
        this.quota = quota;
        this.configCount = configCount;
        this.type = type;
    }

}
