package engine.prototypes.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdProperty"
})
@XmlRootElement(name = "PRD-properties")
public class PRDProperties {
    @XmlElement(name = "PRD-property", required = true)
    protected List<PRDProperty> prdProperty;
}
