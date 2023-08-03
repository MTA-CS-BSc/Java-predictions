package engine.prototypes.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdAction"
})
@XmlRootElement(name = "PRD-actions")
public class PRDActions {
    @XmlElement(name = "PRD-action", required = true)
    protected List<PRDAction> prdAction;
}
