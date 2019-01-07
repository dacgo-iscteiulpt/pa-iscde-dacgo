package outline.extensibility;

import java.util.Map;

import org.eclipse.swt.graphics.Image;

/**
 * The provided image map must provide images with ".png" extensions.
 * The image filenames must go by the following name conventions:
 * 
 * TYPE:
 * enum.png
 * interface.png
 * class.png
 * no_type.png
 * 
 * MODIFIERS:
 * abstract.png
 * static.png
 * final.png
 * static_final.png
 * constructor.png
 * no_modifier.png
 * 
 * ATTRIBUTES/METHODS:
 * attribute_private.png
 * attribute_protected.png
 * attribute_public.png
 *
 * method_private.png
 * method_protected.png
 * method_public.png
 * 
 * OTHERS:
 * outline.png - Sets the outline window icon.
 * package.png
 * packge_empty.png
 * default.png - Image used when type was not treated properly or found.
 */
public class CustomImageMap {
	private Map<String, Image> imageMap;

	public CustomImageMap(Map<String, Image> imageMap) {
		this.imageMap = imageMap;
	}

	public Map<String, Image> getImageMap() {
		return imageMap;
	}

	public void setImageMap(Map<String, Image> imageMap) {
		this.imageMap = imageMap;
	}

}
