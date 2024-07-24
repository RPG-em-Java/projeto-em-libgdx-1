package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.entities.Player;
import com.mygdx.game.ui.BarraVidaPlay;
import com.mygdx.game.ui.Inventario;
import com.mygdx.game.classes.Indio;
import com.mygdx.game.classes.Medico;
import com.mygdx.game.classes.Pistoleiro;
import com.mygdx.game.classes.Xerife;
import com.mygdx.game.entities.NPC;

public class Play implements Screen {

    private Game game;
	private TiledMap mapa1;
    private OrthogonalTiledMapRenderer renderer;
    private SpriteBatch rendererHud;
    private Stage stage;
    private OrthographicCamera camera;
    private Player jogador;
    private NPC buck, django, brunao, nathalia;
    private BitmapFont fontDialogo;
    private Texture backgroundDialogo;

    private Object characterClass;
    private boolean primeiraVez;
    private int classeTipoP;
    private float posicaoX, posicaoY;
    
    private BarraVidaPlay barraVida;
    private Inventario inventarioHud;

    public Play(int classeTipoP, Game game, Player jogador, boolean primeiraVez) {
        this.classeTipoP = classeTipoP;
        this.game = game;
        this.primeiraVez = primeiraVez;
        this.jogador = jogador;
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheavtt.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        fontDialogo = generator.generateFont(parameter);
        generator.dispose();
        
        backgroundDialogo = new Texture("hud/hud_base2.png");
    }

    @Override
    public void show() {
        mapa1 = new TmxMapLoader().load("maps/mapafinal3.tmx");
        renderer = new OrthogonalTiledMapRenderer(mapa1);
        rendererHud = new SpriteBatch();
        camera = new OrthographicCamera();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Texture playerTexture;
        if (primeiraVez == true) {
	        if (classeTipoP == 0) {
	            playerTexture = new Texture("sprites/cowboy_sprites.png");
	            jogador = new Player(playerTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), new Pistoleiro());
	        } else if (classeTipoP == 1) {
	            playerTexture = new Texture("sprites/xerife_sprites.png");
	            jogador = new Player(playerTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), new Xerife());
	        } else if (classeTipoP == 2) {
	            playerTexture = new Texture("sprites/medico_sprites.png");
	            jogador = new Player(playerTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), new Medico());
	        } else if (classeTipoP == 3) {
	            playerTexture = new Texture("sprites/indio_sprites.png");
	            jogador = new Player(playerTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), new Indio());
	        } else {
	        	//jogador.setSprite
	        }
        }

	    //Posicionamento do jogador
	    if (primeiraVez == true) {
	        posicaoX = 55 * jogador.getCollisionLayer().getTileWidth();
	        posicaoY = (jogador.getCollisionLayer().getHeight() - 95) * jogador.getCollisionLayer().getTileHeight();
	    } else {
	    	posicaoX = jogador.getPosicaoX();
	    	Gdx.app.log("posicao x", Float.toString(posicaoX));
	    	posicaoY = jogador.getPosicaoY();
	    }
	    jogador.setPosition(posicaoX, posicaoY);
        
        //Criação dos NPCs
        Texture buckTexture = new Texture("sprites/buck_sprites.png");
        buck = new NPC(buckTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), 1);
        buck.setPosition(75 * buck.getCollisionLayer().getTileWidth(), 
                (buck.getCollisionLayer().getHeight() - 93) * buck.getCollisionLayer().getTileHeight());
        
        Texture djangoTexture = new Texture("sprites/django_sprites.png");
        django = new NPC(djangoTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), 2);
        django.setPosition(225 * django.getCollisionLayer().getTileWidth(), 
                (django.getCollisionLayer().getHeight() - 100) * django.getCollisionLayer().getTileHeight());
        
        Texture brunaoTexture = new Texture("sprites/brunao_sprites.png");
        brunao = new NPC(brunaoTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), 2);
        brunao.setPosition(105 * brunao.getCollisionLayer().getTileWidth(), 
                (brunao.getCollisionLayer().getHeight() - 133) * brunao.getCollisionLayer().getTileHeight());
        
        Texture nathaliaTexture = new Texture("sprites/nathalia_sprites.png");
        nathalia = new NPC(nathaliaTexture, (TiledMapTileLayer) mapa1.getLayers().get(0), 2);
        nathalia.setPosition(160 * nathalia.getCollisionLayer().getTileWidth(), 
                (nathalia.getCollisionLayer().getHeight() - 120) * nathalia.getCollisionLayer().getTileHeight());
        
        barraVida = new BarraVidaPlay(jogador);
        inventarioHud = new Inventario(jogador, stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(jogador.getX(), jogador.getY(), 0);
        camera.zoom = 0.60f;
        camera.update();

        renderer.setView(camera);
        renderer.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            inventarioHud.toggle();
        }

        delta = Gdx.graphics.getDeltaTime();
        inventarioHud.update(delta);
        
        jogador.setPosicaoX(jogador.getX());
        jogador.setPosicaoY(jogador.getY());
        
        barraVida.drawBarraVidaP(rendererHud);
        
        renderer.getBatch().begin();
        
        /*
        if (jogador.getY() >= buck.getY()) {
            jogador.drawPlayer(renderer.getBatch());
            buck.drawNPC(renderer.getBatch());
        } else {
            buck.drawNPC(renderer.getBatch());
            jogador.drawPlayer(renderer.getBatch());
        }
        */
        
        //Desenho das entidades no mapa
        buck.drawNPC(renderer.getBatch());
        django.drawNPC(renderer.getBatch());
        brunao.drawNPC(renderer.getBatch());
        nathalia.drawNPC(renderer.getBatch());
        jogador.drawPlayer(renderer.getBatch());
        
        if (jogador.getX() > brunao.getX()-200 && jogador.getX() < brunao.getX()+200) {
        	if (jogador.getY() > brunao.getY()-200 && jogador.getY() < brunao.getY()+200) {
        		renderer.getBatch().draw(backgroundDialogo, brunao.getX()-60, brunao.getY()+33, 500, 20);
        		fontDialogo.draw(renderer.getBatch(), "> SOU NÁUTICO ATÉ MORRER! VAMOS SUBIR, NÁUTICO!", brunao.getX()+25, brunao.getY()+45);
        	}
        }
        
        if (jogador.getX() > nathalia.getX()-200 && jogador.getX() < nathalia.getX()+200) {
        	if (jogador.getY() > nathalia.getY()-200 && jogador.getY() < nathalia.getY()+200) {
        		renderer.getBatch().draw(backgroundDialogo, nathalia.getX()-60, nathalia.getY()+33, 400, 20);
        		fontDialogo.draw(renderer.getBatch(), "> TRI TRI TRI TRICOLOR, SANTA!", nathalia.getX()+25, nathalia.getY()+45);
        	}
        }
        
        renderer.getBatch().end();
        
     // Dentro do método render() da classe Play, logo após renderer.getBatch().end();

        if (jogador.getBounds().overlaps(buck.getBounds())) {
            game.setScreen(new CombatScreen(jogador, buck, game));
        }
        if (jogador.getBounds().overlaps(django.getBounds())) {
            game.setScreen(new CombatScreen(jogador, django, game));
        }

        
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        
        inventarioHud.update(Gdx.graphics.getDeltaTime());
        inventarioHud.drawInventario(rendererHud);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        mapa1.dispose();
        stage.dispose();
        renderer.dispose();
        jogador.getTexture().dispose();
        buck.getTexture().dispose();
        django.getTexture().dispose();
        brunao.getTexture().dispose();
        nathalia.getTexture().dispose();
        inventarioHud.dispose();
        fontDialogo.dispose();
    }
}
