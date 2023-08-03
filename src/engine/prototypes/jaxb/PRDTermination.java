package engine.prototypes.jaxb;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdByTicksOrPRDBySecond"
})

@XmlRootElement(name = "PRD-termination")
public class PRDTermination {

    @XmlElements({
        @XmlElement(name = "PRD-by-ticks", type = PRDByTicks.class),
        @XmlElement(name = "PRD-by-second", type = PRDBySecond.class)
    })
    protected List<Object> prdByTicksOrPRDBySecond;
}
