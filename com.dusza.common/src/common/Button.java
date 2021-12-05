package common;

public class Button {

    /*
        A button osztály a játékpályán lévő egyetlen mező állapotát tárolja.
        Az IsPressed(), valamint a SetPressed() függvény(ek) az adott gomb(ok) állapotát vizsgálja, valamint módosítja.
        A 'char id' változóban eltároljuk és az ezzel kapcsolatos függvényekkel lekezeljük hogy az adott sorban éppen a gombok megegyeznek-e.
    */

    private char id;
    private boolean pressed;

    public Button(char id) {
        this.id = id;
    }

    public Button(char id, boolean pressed) {
        this.id = id;
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }
}
