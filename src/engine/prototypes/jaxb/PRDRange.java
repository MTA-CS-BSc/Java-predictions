package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PRD-range")
public class PRDRange {

    @XmlAttribute(name = "to", required = true)
    protected double to;
    @XmlAttribute(name = "from", required = true)
    protected double from;
}
