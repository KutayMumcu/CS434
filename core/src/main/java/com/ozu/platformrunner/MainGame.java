package com.ozu.platformrunner;

import com.badlogic.gdx.Game;

/**
 * LibGDX'in Game sınıfını genişletiyoruz. Bu sınıf, farklı oyun ekranlarını
 * (MenuScreen, GameScreen, EndScreen vb.) yönetmemizi sağlar.
 */
public class MainGame extends Game {

    /**
     * Uygulama ilk oluşturulduğunda çağrılır.
     * Burada oyunun başlangıç ekranını (GameScreen) ayarlıyoruz.
     */
    @Override
    public void create() {
        // Oyunun ana ekranını başlatıyoruz.
        // Artık bu ekranda karakter hareketleri, fizik ve çizim işlemleri yapılacak.
        setScreen(new GameScreen());
    }

    /*
     * Game sınıfını kullandığımız için render() metodu GameScreen içinden çağrılır.
     * Bu sınıfta render, resize ve pause/resume metotlarına artık ihtiyacımız yok.
     */

    @Override
    public void dispose() {
        super.dispose();
        // setScreen metodu ile atanan mevcut ekranın dispose metodu çağrılır.
    }
}
