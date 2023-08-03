package engine.prototypes.jaxb;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdRule"
})
@XmlRootElement(name = "PRD-rules")
public class PRDRules {

    @XmlElement(name = "PRD-rule", required = true)
    protected List<PRDRule> prdRule;
}
