package in.srain.cube.views.banner;

public interface PagerIndicator {
    int getCurrentIndex();

    int getTotal();

    void setNum(int i);

    void setSelected(int i);
}
