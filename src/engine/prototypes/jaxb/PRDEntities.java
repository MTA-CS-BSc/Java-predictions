package engine.prototypes.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdEntity"
})
@XmlRootElement(name = "PRD-entities")
public class PRDEntities {

    @XmlElement(name = "PRD-entity", required = true)
    protected List<PRDEntity> prdEntity;

}
