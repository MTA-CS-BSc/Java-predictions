package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PRD-divide")
public class PRDDivide {

    @XmlAttribute(name = "arg2", required = true)
    protected String arg2;
    @XmlAttribute(name = "arg1", required = true)
    protected String arg1;
}
