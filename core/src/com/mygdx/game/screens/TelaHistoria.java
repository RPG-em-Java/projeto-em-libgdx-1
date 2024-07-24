package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.entities.Player;

public class TelaHistoria implements Screen {
    private Game game;
    private int classeTipo;
    private Player player;
    private boolean primeiraVez;
    
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private BitmapFont font;

    public TelaHistoria(int classeTipo, Game game, Player player, boolean primeiraVez) {
        this.game = game;
        this.classeTipo = classeTipo;
        this.player = player;
        this.primeiraVez = primeiraVez;
        
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        this.font = new BitmapFont();
        this.camera = new OrthographicCamera();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("fonts/pixthulhu-ui.json"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Label textoHistoria = new Label("Você é um caçador de recompensas que está atrás do alvo do seu contrato de procurado. Estava barato demais para a dificuldade que apresentava, mas mesmo assim você aceitou. O seu objetivo era matar o Matador de Bandoleiros, o Django. Ande pelas terras desérticas e encontre Django.", labelStyle);
        textoHistoria.setWrap(true);
        textoHistoria.setWidth(Gdx.graphics.getWidth() - 40);
        textoHistoria.setPosition(20, Gdx.graphics.getHeight() - 150);
        //textoHistoria.setSize(200,200);

        TextButton continueButton = new TextButton("Clique aqui para continuar", skin);
        continueButton.setPosition(Gdx.graphics.getWidth() / 2 - continueButton.getWidth() / 2, 50);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Play(classeTipo, game, player, primeiraVez)); // Altere os parâmetros conforme necessário
            }
        });

        stage.addActor(textoHistoria);
        stage.addActor(continueButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        font.dispose();
    }
}
