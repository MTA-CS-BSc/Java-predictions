package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdPopulation",
    "prdProperties"
})
@XmlRootElement(name = "PRD-entity")
public class PRDEntity {

    @XmlElement(name = "PRD-population")
    protected int prdPopulation;
    @XmlElement(name = "PRD-properties", required = true)
    protected PRDProperties prdProperties;
    @XmlAttribute(name = "name", required = true)
    protected String name;
}
