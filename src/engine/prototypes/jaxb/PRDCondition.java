package engine.prototypes.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdCondition"
})
@XmlRootElement(name = "PRD-condition")
public class PRDCondition {

    @XmlElement(name = "PRD-condition")
    protected List<PRDCondition> prdCondition;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "singularity", required = true)
    protected String singularity;
    @XmlAttribute(name = "operator")
    protected String operator;
    @XmlAttribute(name = "property")
    protected String property;
    @XmlAttribute(name = "logical")
    protected String logical;
    @XmlAttribute(name = "entity")
    protected String entity;
}
