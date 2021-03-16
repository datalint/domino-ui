package org.dominokit.domino.ui.scroll;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.BaseDominoElement;

import static org.jboss.elemento.Elements.div;

/**
 * A component that wraps the {@link HTMLDivElement} to make it auto scrollable when the content exceeds the component fixed width or height
 */
public class AutoScrollPanel extends BaseDominoElement<HTMLDivElement, AutoScrollPanel> {

    private HTMLDivElement element = div().css("auto-scroll-panel").element();

    /**
     *
     * @return new AutoScrollPanel instance
     */
    public static AutoScrollPanel create(){
        return new AutoScrollPanel();
    }

    public AutoScrollPanel() {
        init(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLDivElement element() {
        return element;
    }
}
