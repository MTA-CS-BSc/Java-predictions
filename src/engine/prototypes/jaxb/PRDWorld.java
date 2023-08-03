package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdEvironment",
    "prdEntities",
    "prdRules",
    "prdTermination"
})

@XmlRootElement(name = "PRD-world")
public class PRDWorld {
    @XmlElement(name = "PRD-evironment", required = true)
    protected PRDEvironment prdEvironment;
    @XmlElement(name = "PRD-entities", required = true)
    protected PRDEntities prdEntities;
    @XmlElement(name = "PRD-rules", required = true)
    protected PRDRules prdRules;
    @XmlElement(name = "PRD-termination", required = true)
    protected PRDTermination prdTermination;
}
