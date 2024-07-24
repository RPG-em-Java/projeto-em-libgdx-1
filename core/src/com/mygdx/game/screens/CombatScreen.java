package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.NPC;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class CombatScreen implements Screen {

    private Game game;
	private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeVidaJogador, shapeVidaJogadorFundo, shapeVidaNPC, shapeVidaNPCFundo;
    private ShapeRenderer shapeEnergiaJogador, shapeEnergiaJogadorFundo, shapeEnergiaNPC, shapeEnergiaNPCFundo;
    private BitmapFont font;
    private Player player;
    private NPC npc;
    private String ataqueComum, habilidade1, nomeJogador;
    private String[] ataques = {
    	"Tiro de revolver", 
    	"Tiro de escopeta", 
  		"Jogada de facas", 
   		"Pancada de Tomahawk"
    		};
    private String[] habilidades = {
    	"Na mosca",
    	"Espalha-dores",
    	"Circunsição",
    	"Punição ancestral"
    };
    private String[] nomesClasses = {
    		"Pistoleiro",
    		"Xerife",
    		"Médico",
    		"Índio"
    };
    
    private boolean playerTurn;
    private boolean gameOver;
    private boolean victory;
    private float gameOverTimer;
    private float victoryTimer;
    private float turnoTimer;
    
    private Texture playerTexture;
    private Texture npcTexture;
    private Texture attackTexture;
    private Texture backgroundCombate;
    private Texture backgroundHud;
    
    public CombatScreen(Player player, NPC npc, Game game) {
        this.game = game;
    	this.player = player;
        this.npc = npc;
        this.playerTurn = true;
        this.gameOver = false;
        this.victory = false;
        this.gameOverTimer = 0;
        this.victoryTimer = 0;
        this.turnoTimer = 0;
        
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        
        shapeVidaJogador = new ShapeRenderer();
        shapeVidaJogadorFundo = new ShapeRenderer();
        shapeVidaNPC = new ShapeRenderer();
        shapeVidaNPCFundo = new ShapeRenderer();
        shapeEnergiaJogador = new ShapeRenderer();
        shapeEnergiaJogadorFundo = new ShapeRenderer();
        shapeEnergiaNPC = new ShapeRenderer();
        shapeEnergiaNPCFundo = new ShapeRenderer();
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheavtt.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter);
        generator.dispose();
        
        playerTexture = player.getSpriteCombate();
        npcTexture = npc.getTexturaNPC();
        attackTexture = new Texture("combate/hitmark.png");
        backgroundCombate = new Texture("combate/fundoCombate.png");
        backgroundHud = new Texture("hud/hud_base1.png");
        
        for (int i = 0; i < 4; i++) {
        	if (player.getClasseTipo() == i){
        		ataqueComum = ataques[i];
        		habilidade1 = habilidades[i];
        		nomeJogador = nomesClasses[i];
        	}
        }
        
    }

    @Override
    public void show() {
    	camera.position.set(640, 360, 0);
    	camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        double barWidth = 500; // Largura total da barra de vida
        double barHeight = 30;

        //Define o tamanho das barras de vida de acordo com a vida dos personagens
        double healthPercentageJogador = player.getVidaAtual() / player.getVidaMax();
        double currentBarWidthVidaJogador = barWidth * healthPercentageJogador;
        double energiaPercentageJogador = player.getEnergiaAtual() / player.getEnergiaMax();
        double currentBarWidthEnergiaJogador = barWidth * energiaPercentageJogador;
        
        double healthPercentageNPC = npc.getVidaAtual() / npc.getVidaMax();
        double currentBarWidthVidaNPC = barWidth * healthPercentageNPC;
        double energiaPercentageNPC = npc.getEnergiaAtual() / npc.getEnergiaMax();
        double currentBarWidthEnergiaNPC = barWidth * energiaPercentageNPC;

        if (gameOver) {
            game.setScreen(new GameOverScreen(game));
            	
        } else if (victory) {
            victoryTimer += delta;
            if (victoryTimer > 1) {
                // Voltar ao jogo após vitória
                game.setScreen(new VictoryScreen(player, game));
            }
        } else {
            if (playerTurn) {
                handlePlayerInput();
            } else {
            	turnoTimer += delta;
            	if (turnoTimer > 1) {
            		npcAttack();
                	playerTurn = true;
            	}
            }
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        //shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundCombate, 0, 0, 1280, 720);
        batch.draw(backgroundHud, 0-400, 370, 2400, 700);
        
        font.draw(batch, nomeJogador, 400, 530);
        font.draw(batch, "Vida: " + player.getVidaAtual() + "/" + player.getVidaMax(), 20, 690);
        font.draw(batch, "Energia: " + player.getEnergiaAtual() + "/" + player.getEnergiaMax(), 20, 610);
        font.draw(batch, npc.getNomeNPC(), 800, 530);
        font.draw(batch, "Vida: " + npc.getVidaAtual() + "/" + npc.getVidaMax(), 1070, 690);
        font.draw(batch, "Energia: " + npc.getEnergiaAtual() + "/" + npc.getEnergiaMax(), 1070, 610);
        font.draw(batch, "[A] para um ataque comum / [H] para usar a habilidade especial", 10, 20);
        
        batch.draw(playerTexture, 160, 75, 400, 400);
        batch.draw(npcTexture, 750, 75, 400, 400);
        //shapeRenderer.setColor(Color.RED);
        batch.end();
        
        //Shape de fundo das barras do jogador
        shapeVidaJogadorFundo.begin(ShapeRenderer.ShapeType.Filled);
        shapeVidaJogadorFundo.setColor(Color.WHITE);
        shapeVidaJogadorFundo.rect(20, 630, (float) barWidth, (float) barHeight);
        shapeVidaJogadorFundo.end();
        shapeEnergiaJogadorFundo.begin(ShapeRenderer.ShapeType.Filled);
        shapeEnergiaJogadorFundo.setColor(Color.WHITE);
        shapeEnergiaJogadorFundo.rect(20, 550, (float) barWidth, (float) barHeight);
        shapeEnergiaJogadorFundo.end();
        //Shape de fundo das barras do NPC
        shapeVidaNPCFundo.begin(ShapeRenderer.ShapeType.Filled);
        shapeVidaNPCFundo.setColor(Color.WHITE);
        shapeVidaNPCFundo.rect(760, 630, (float) barWidth, (float) barHeight);
        shapeVidaNPCFundo.end();
        shapeEnergiaNPCFundo.begin(ShapeRenderer.ShapeType.Filled);
        shapeEnergiaNPCFundo.setColor(Color.WHITE);
        shapeEnergiaNPCFundo.rect(760, 550, (float) barWidth, (float) barHeight);
        shapeEnergiaNPCFundo.end();
        
        //Shape das barras de vida e energia do jogador
        shapeVidaJogador.begin(ShapeRenderer.ShapeType.Filled);
        shapeVidaJogador.setColor(Color.RED);
        shapeVidaJogador.rect(20, 630, (float) currentBarWidthVidaJogador, (float) barHeight);
        shapeVidaJogador.end();
        shapeEnergiaJogador.begin(ShapeRenderer.ShapeType.Filled);
        shapeEnergiaJogador.setColor(Color.ORANGE);
        shapeEnergiaJogador.rect(20, 550, (float) currentBarWidthEnergiaJogador, (float) barHeight);
        shapeEnergiaJogador.end();
        //Shape das barras de vida e energia do NPC
        shapeVidaJogador.begin(ShapeRenderer.ShapeType.Filled);
        shapeVidaJogador.setColor(Color.RED);
        shapeVidaJogador.rect(760, 630, (float) currentBarWidthVidaNPC, (float) barHeight);
        shapeVidaJogador.end();
        shapeEnergiaJogador.begin(ShapeRenderer.ShapeType.Filled);
        shapeEnergiaJogador.setColor(Color.ORANGE);
        shapeEnergiaJogador.rect(760, 550, (float) currentBarWidthEnergiaNPC, (float) barHeight);
        shapeEnergiaJogador.end();

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.end();
        
    }

    private void handlePlayerInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            npc.setVidaAtual(npc.getVidaAtual() - player.getDano());
            if (npc.getVidaAtual() <= 0) {
            	npc.setVidaAtual(0);
            	victory = true;
            }
            playerTurn = false;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            if (player.getEnergiaAtual() >= 4) {
                npc.setVidaAtual(npc.getVidaAtual() - (player.getDano() * 2));
                player.setEnergiaAtual(player.getEnergiaAtual() - 4);
                if (npc.getVidaAtual() <= 0) {
                    npc.setVidaAtual(0);
                	victory = true;
                }
                playerTurn = false;
            }
        }
    }

    private void npcAttack() {
        player.setVidaAtual(player.getVidaAtual() - npc.getDano());
        if (player.getVidaAtual() <= 0) {
            gameOver = true;
        }
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
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        //shapeRenderer.dispose();
        font.dispose();
        playerTexture.dispose();
        npcTexture.dispose();
        backgroundCombate.dispose();
        backgroundHud.dispose();
    }
}
