package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PRD-value")
public class PRDValue {
    @XmlAttribute(name = "random-initialize", required = true)
    protected boolean randomInitialize;
    @XmlAttribute(name = "init")
    protected String init;
}
