package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdActions",
    "prdActivation"
})
@XmlRootElement(name = "PRD-rule")
public class PRDRule {

    @XmlElement(name = "PRD-actions", required = true)
    protected PRDActions prdActions;
    @XmlElement(name = "PRD-activation")
    protected PRDActivation prdActivation;
    @XmlAttribute(name = "name", required = true)
    protected String name;
}
