package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdDivide",
    "prdMultiply",
    "prdCondition",
    "prdThen",
    "prdElse"
})
@XmlRootElement(name = "PRD-action")
public class PRDAction {

    @XmlElement(name = "PRD-divide")
    protected PRDDivide prdDivide;
    @XmlElement(name = "PRD-multiply")
    protected PRDMultiply prdMultiply;
    @XmlElement(name = "PRD-condition")
    protected PRDCondition prdCondition;
    @XmlElement(name = "PRD-then")
    protected PRDThen prdThen;
    @XmlElement(name = "PRD-else")
    protected PRDElse prdElse;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "type", required = true)
    protected String type;
    @XmlAttribute(name = "result-prop")
    protected String resultProp;
    @XmlAttribute(name = "property")
    protected String property;
    @XmlAttribute(name = "entity", required = true)
    protected String entity;
    @XmlAttribute(name = "by")
    protected String by;
}
