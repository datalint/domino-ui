package org.dominokit.domino.ui.infoboxes;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.icons.BaseIcon;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.ElementUtil;
import org.dominokit.domino.ui.utils.HasBackground;
import org.jboss.elemento.IsElement;

import static java.util.Objects.nonNull;

/**
 * Container for displaying information with icons
 * <p>
 * This component provides a container which allows showing information with icons and effects
 * <p>
 * Customize the component can be done by overwriting classes provided by {@link InfoBoxStyles}
 *
 * <p>For example: </p>
 * <pre>
 *     InfoBox.create(Icons.ALL.shopping_cart(), "NEW ORDERS", "125");
 * </pre>
 *
 * @see BaseDominoElement
 * @see HasBackground
 */
public class InfoBox extends BaseDominoElement<HTMLDivElement, InfoBox> implements HasBackground<InfoBox>, IsElement<HTMLDivElement> {

    private final DominoElement<HTMLDivElement> iconElement = DominoElement.div().css("icon");
    private final DominoElement<HTMLDivElement> titleElement = DominoElement.div().css("text");
    private final DominoElement<HTMLDivElement> valueElement = DominoElement.div()
            .css("number")
            .css("count-to");
    private final DominoElement<HTMLDivElement> infoContent = DominoElement.div()
            .css("info-content")
            .appendChild(titleElement)
            .appendChild(valueElement);

    private final DominoElement<HTMLDivElement> root = DominoElement.div()
            .css("info-box")
            .elevate(Elevation.LEVEL_1)
            .appendChild(iconElement)
            .appendChild(infoContent);

    private final HTMLElement icon;

    private Color counterBackground;

    private Color iconBackground;
    private HoverEffect hoverEffect;
    private Flip flip = Flip.LEFT;
    private Color iconColor;

    public InfoBox(HTMLElement icon, String title, String value) {
        iconElement.appendChild(icon);
        titleElement.setTextContent(title);
        if (nonNull(value)) {
            valueElement.setTextContent(value);
        }
        init(this);
        this.icon = icon;

    }

    public InfoBox(BaseIcon<?> icon, String title, String value) {
        this(icon.element(), title, value);
    }

    public InfoBox(HTMLElement icon, String title) {
        this(icon, title, null);
    }

    public InfoBox(BaseIcon<?> icon, String title) {
        this(icon, title, null);
    }

    /**
     * Creates info box with icon, title and value
     *
     * @param icon  The {@link BaseIcon}
     * @param title the title
     * @param value the value
     * @return new instance
     */
    public static InfoBox create(BaseIcon<?> icon, String title, String value) {
        return new InfoBox(icon, title, value);
    }

    /**
     * Creates info box with icon element, title and value
     *
     * @param icon  The {@link HTMLElement} icon
     * @param title the title
     * @param value the value
     * @return new instance
     */
    public static InfoBox create(HTMLElement icon, String title, String value) {
        return new InfoBox(icon, title, value);
    }

    /**
     * Creates info box with icon and title
     *
     * @param icon  The {@link HTMLElement} icon
     * @param title the title
     * @return new instance
     */
    public static InfoBox create(HTMLElement icon, String title) {
        return new InfoBox(icon, title);
    }

    /**
     * Creates info box with icon and title
     *
     * @param icon  The {@link BaseIcon}
     * @param title the title
     * @return new instance
     */
    public static InfoBox create(BaseIcon<?> icon, String title) {
        return new InfoBox(icon, title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InfoBox setBackground(Color background) {
        if (nonNull(counterBackground))
            style().remove(counterBackground.getBackground());
        style().add(background.getBackground());
        this.counterBackground = background;

        return this;
    }

    /**
     * Sets the background of the icon element
     *
     * @param background The {@link Color} of the icon
     * @return same instance
     */
    public InfoBox setIconBackground(Color background) {
        if (nonNull(iconBackground))
            Style.of(iconElement).remove(iconBackground.getBackground());
        Style.of(iconElement).add(background.getBackground());
        this.iconBackground = background;

        return this;
    }

    /**
     * Sets the hover effect
     *
     * @param effect the {@link HoverEffect}
     * @return same instance
     */
    public InfoBox setHoverEffect(HoverEffect effect) {
        if (nonNull(hoverEffect))
            style().remove(hoverEffect.effectStyle);
        this.hoverEffect = effect;
        style().add(hoverEffect.effectStyle);

        return this;
    }

    /**
     * Removes the hover effects
     *
     * @return same instance
     */
    public InfoBox removeHoverEffect() {
        if (nonNull(hoverEffect)) {
            style().remove(hoverEffect.effectStyle);
            this.hoverEffect = null;
        }

        return this;
    }

    /**
     * Puts the icon on the left and the title on the right
     *
     * @return same instance
     */
    public InfoBox flipLeft() {
        style().remove(flip.flipStyle);
        this.flip = Flip.LEFT;
        style().add(this.flip.flipStyle);

        return this;
    }

    /**
     * Puts the icon on the right and the title on the left
     *
     * @return same instance
     */
    public InfoBox flipRight() {
        style().remove(flip.flipStyle);
        this.flip = Flip.RIGHT;
        style().add(this.flip.flipStyle);
        return this;
    }

    /**
     * Changes the position of the icon and title based on the current position; i.e. if the icon is left, position it to right
     *
     * @return same instance
     */
    public InfoBox flip() {
        style().remove(flip.flipStyle);
        if (Flip.LEFT.equals(this.flip)) {
            this.flip = Flip.RIGHT;
        } else {
            this.flip = Flip.LEFT;
        }
        style().add(this.flip.flipStyle);

        return this;
    }

    /**
     * Sets the color of the icon
     *
     * @param color The {@link Color} of the icon
     * @return same instance
     */
    public InfoBox setIconColor(Color color) {
        if (nonNull(iconColor) && nonNull(icon))
            Style.of(icon).remove(iconColor.getStyle());
        if (nonNull(icon)) {
            this.iconColor = color;
            Style.of(icon).add(this.iconColor.getStyle());
        }

        return this;
    }

    /**
     * Sets the icon
     * @param element the {@link HTMLElement} icon
     * @return same instance
     */
    public InfoBox setIcon(HTMLElement element) {
        ElementUtil.clear(iconElement);
        iconElement.appendChild(element);
        return this;
    }

    /**
     * Removes the shadow of the box
     * @return same instance
     */
    public InfoBox removeShadow() {
        style().removeShadow();
        return this;
    }

    /**
     * Sets the value
     * @param value  the new value
     * @return same instance
     */
    public InfoBox setValue(String value) {
        getValueElement().setTextContent(value);
        return this;
    }

    /**
     * Sets the title
     * @param title the new title
     * @return same instance
     */
    public InfoBox setTitle(String title) {
        getTitleElement().setTextContent(title);
        return this;
    }

    /**
     * Sets the icon
     * @param icon the {@link BaseIcon}
     * @return same instance
     */
    public InfoBox setIcon(BaseIcon<?> icon) {
        getIconElement().clearElement().appendChild(icon);
        return this;
    }

    /**
     * @return The icon element
     */
    public DominoElement<HTMLDivElement> getIconElement() {
        return iconElement;
    }

    /**
     * @return The title element
     */
    public DominoElement<HTMLDivElement> getTitleElement() {
        return titleElement;
    }

    /**
     * @return The value element
     */
    public DominoElement<HTMLDivElement> getValueElement() {
        return valueElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLDivElement element() {
        return root.element();
    }

    /**
     * An enum representing the hover effect
     */
    public enum HoverEffect {
        ZOOM(InfoBoxStyles.HOVER_ZOOM_EFFECT),
        EXPAND(InfoBoxStyles.HOVER_EXPAND_EFFECT);

        private final String effectStyle;

        HoverEffect(String effectStyle) {
            this.effectStyle = effectStyle;
        }
    }

    /**
     * An enum representing the direction of the flip done on the icon and the title elements
     */
    public enum Flip {
        RIGHT(InfoBoxStyles.INFO_BOX_3),
        LEFT(InfoBoxStyles.INFO_BOX);

        private final String flipStyle;

        Flip(String flipStyle) {
            this.flipStyle = flipStyle;
        }
    }
}
