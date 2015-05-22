package tbr.main;

public class DisplayModeWrapper implements Comparable<DisplayModeWrapper> {

	public int width;
	public int height;
	
	public DisplayModeWrapper(int w, int h) {
		width = w;
		height = h;
	}

	@Override
	public int hashCode() {
		return width * height;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof DisplayModeWrapper) {
			DisplayModeWrapper temp = (DisplayModeWrapper) obj;
			return this.width == temp.width && this.height == temp.height;
		}
		return false;
	}
	
	@Override
	public int compareTo(DisplayModeWrapper obj) {
		if(width - obj.width > 0)
			return 1;
		else if(width - obj.width < 0)
			return -1;
		else
			if(height - obj.height > 0)
				return 1;
			else
				return -1;
	}
	
	@Override
	public String toString() {
		return width+"x"+height;
	}
	
}
