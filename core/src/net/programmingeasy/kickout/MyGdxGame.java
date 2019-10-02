package net.programmingeasy.kickout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;



public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	BitmapFont fnt, fnt2, fnt3;
	boolean animStarted = false;
	float w, h;
	float angle = 0;
	float speed = 2;
	float k;
	boolean game_started = false, is_loosed = false, tmp_loose = false;
	float labelAlpha = 0, colorAlpha = 1f;
	int selectorSpace;
	int record = 0;
	float sZ = 0.25f;
	float animSpeed = 5;
	float tempSz;
	int scoreUpd = 1;
	int score = 0;
	float last_ang = 0;

	Color tC;
	Circle[] circles = new Circle[2];
	Color[] commonColors = {new Color(245f / 256, 147f / 256f, 66f / 256f, 1), new Color(255 / 256f, 0, 234 / 256f, 1),
			new Color(1f, 0.2f, 0.2f, 1), new Color(1f, 1f, 0.2f, 1), new Color(0.2f, 0.2f, 1f, 1),
			new Color(0.1f, 1f, 0.1f, 1), new Color(0.1f, 1f, 1f, 1),
			new Color(91 / 256f, 166 / 256f, 0, 1), new Color(99 / 256f, 0, 166 / 256f, 1)};

	public int rand(int a, int b) {
		return (int)(Math.floor(Math.random() * b) + a);
	}

	public Color[] genNewColors(Color[] old, int ln) {
		Color[] nw = new Color[ln];
		int st = 1;
		if (old.length == 0)
			st = 0;
		else {
			Color c = old[rand(0, old.length)];
			nw[0] = c;
		}
		for (int i = st; i < ln; i++) {
			nw[i] = commonColors[rand(0, commonColors.length)];
		}
		return nw;
	}

	public void restart() {
		circles = new Circle[2];
		tmp_loose = false;
		animStarted = false;
		angle = 0;
		speed = 2;
		is_loosed = false;
		labelAlpha = 0;
		sZ = 0.25f;
		animSpeed = 5;
		scoreUpd = 1;
		colorAlpha = 1f;
		score = 0;
		last_ang = 0;
		tC = commonColors[rand(0, commonColors.length)];
		float one_ang = 360f / 6;
		circles[1] = new Circle(w / 2, h / 2, w * sZ * 1.75f, 25, -one_ang / 2, genNewColors(new Color[]{tC}, 6));
		selectorSpace = (int)(w * sZ) / 5;
		circles[1].c[2] = new Color(1, 0, 0, 1);

	}

	@Override
	public void create () {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		k = w / 1080;
		Preferences prefs = Gdx.app.getPreferences("prefs");
		record = prefs.getInteger("record", 0);
		restart();
		System.out.println(circles[1].c[0] + " " + circles[1].c[1] + " " + circles[1].c[2]);
		//circles[1].rot = -30;
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		String FONT_CHARACTERS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890][_!$%#@|\\\\/?-+=()*&.;,{}\\\"´`'<>";
		fnt = new BitmapFont(Gdx.files.internal("Calibri.fnt"), false);
		fnt3 = new BitmapFont(Gdx.files.internal("Calibri110.fnt"), false);
		fnt.getData().setScale(k, k);
		fnt2 = new BitmapFont();
		fnt2.getData().setScale(k, k);
		fnt3.getData().setScale(k, k);
	}
	public void drawDirSelector(ShapeRenderer sR, double ang, float x, float y, int space, int count) {
		double rads = Math.PI / 180 * ang;
		float startSize = 7 * (w / 600);
		int s = 0;
		for (int i = 0; i < count; i++) {
			float y2 = y + (float)Math.cos(rads) * s;
			float x2 = (float) Math.sin(rads) * s + x;
			sR.circle(x2, y2, startSize);
			startSize += 0.6 * (w / 600);
			s += space;

		}

	}

	public void drawSectorizedCircle(ShapeRenderer sR, float rotA, float x, float y, float rad, float w, Color[] colors) {
		float ang = 360.0f / colors.length;
		Color c = sR.getColor();
		c.a = colorAlpha;
		for (int i = 0; i < colors.length; i++) {
			colors[i].a = colorAlpha;
			sR.setColor(colors[i]);
			sR.arc(x, y, rad, -(rotA + i * ang), ang);
		}
		sR.setColor(new Color(0, 0, 0, 1));
		sR.circle(x, y, rad - w * 2);
		sR.setColor(c);
	}
	@Override
	public void resize(int width, int height) {
	}

	public void loose() {
		is_loosed = true;
	}

	@Override
	public void render () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (game_started && !is_loosed) {
			speed = Math.max(speed, 2 + (float)score / 20);
			if (Gdx.input.justTouched() && !animStarted) {
				animStarted = true;
				float one_ang = 360f / 6;
				float ang = 360f / circles[1].c.length;
				last_ang = angle;
				int num = (int) angle / (int) ang;
				if (circles[1].c[num].r == tC.r && circles[1].c[num].g == tC.g && circles[1].c[num].b == tC.b) {
					score += scoreUpd;
					Preferences prefs = Gdx.app.getPreferences("prefs");
					record = Math.max(prefs.getInteger("record", 0), score);
					prefs.putInteger("record", record);
					prefs.flush();
					circles[0] = new Circle(w / 2, h / 2, w * sZ * 2.5f, 25, -one_ang / 2, genNewColors(circles[1].c, 6));
				} else {
					tmp_loose = true;
				}
			/*float rot = circles[1].rot;
			circles[2] = new Circle(circles[1]);
			circles[2].rad = w * sZ;
			circles[2].rot = rot;
			circles[1].rad = w * sZ * 2.5f;
			circles[1].c = genNewColors(circles[1].c, circles[1].c.length);*/
			}
			if (!animStarted) {
				//System.out.println(last_ang);

				angle = (angle + speed);
				if (angle <= last_ang && angle + speed > last_ang && score > 0) {
					tmp_loose = true;
				}
				angle %= 360;
			} else {
				if (tmp_loose && colorAlpha > 0){
					colorAlpha = Math.max(0, colorAlpha - 0.03f);
				} else if (!tmp_loose && circles[1].rad > w * sZ) {
					circles[1].rad -= animSpeed;
					if (circles[0].rad > w * sZ * 1.75f)
						circles[0].rad -= animSpeed;
				} else {
					if (tmp_loose)
						loose();
					else {
						animStarted = false;
						tC = circles[0].c[rand(0, circles[0].c.length)];
						circles[1] = new Circle(circles[0]);
						circles[1].rad = circles[0].rad;
						circles[0].visible = false;

					}
				}
			}
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			for (int i = 0; i < circles.length; i++) {
				if (circles[i] != null) {
					//System.out.println(circles[i].rot + " " + circles[i].x + " " + circles[i].y + " " + circles[i].rad + " " + circles[i].w);
					if (circles[i].visible)
						drawSectorizedCircle(shapeRenderer, circles[i].rot, circles[i].x, circles[i].y, circles[i].rad, circles[i].w * k, circles[i].c);
				}
			}
			tC.a = colorAlpha;
			shapeRenderer.setColor(tC);
			//drawDirSelector(shapeRenderer, angle, w / 2, h / 2, 10, 5);
			drawDirSelector(shapeRenderer, angle, w / 2, h / 2, selectorSpace, 5);
			shapeRenderer.end();
			batch.begin();
			//System.out.println(circles[1].c[2]);
			fnt.getData().setScale(1.2f * k, 1.2f * k);
			GlyphLayout gl = new GlyphLayout(fnt3, score + "");
			fnt.setColor(new Color(1, 1, 1, colorAlpha));
			fnt.draw(batch, score + "", w / 2 - new GlyphLayout(fnt, score + "").width / 2, circles[1].y + w * sZ * 2.5f + 1 * gl.height);
			fnt.getData().setScale(0.8f * k, 0.8f * k);
			fnt.draw(batch, "Record: " + record, 10, new GlyphLayout(fnt, "Record: " + record).height + 20);
			batch.end();
		} else if (!game_started) {
			batch.begin();
			GlyphLayout gl = new GlyphLayout(fnt, "Tap to start");
			fnt.setColor(new Color(1f, 1f, 1f, (float)Math.abs(Math.sin(labelAlpha))));
			labelAlpha = (labelAlpha + 0.01f) % 360;
			fnt.getData().setScale(k, k);
			fnt.draw(batch, "Tap to start", w / 2 - gl.width / 2, h / 2 - gl.height / 2);
			batch.end();
			if (Gdx.input.justTouched()) {
				game_started = true;
			}
		} else if (is_loosed) {
			batch.begin();
			GlyphLayout gl = new GlyphLayout(fnt3, "Tap to restart");
			labelAlpha = Math.min(1f, labelAlpha + 0.01f);
			fnt.setColor(new Color(1f, 1f, 1f, labelAlpha));
			fnt3.setColor(new Color(1f, 1f, 1f, labelAlpha));
			fnt3.draw(batch, "Tap to restart", w / 2 - gl.width / 2, h / 2 - gl.height / 2 + 120);
			GlyphLayout gl2 = new GlyphLayout(fnt, "Score: " + score);
			fnt.draw(batch, "Score: " + score, w / 2 - gl2.width / 2, h / 2 - gl.height + 25 - gl2.height / 2);
			gl2 = new GlyphLayout(fnt, "Record: " + record);
			fnt.draw(batch, "Record: " + record, w / 2 - gl2.width / 2, h / 2 - gl.height - 60 - gl2.height / 2);
			batch.end();
			if (Gdx.input.justTouched() && labelAlpha >= 0.5f) {
				game_started = true;
				is_loosed = false;
				restart();
			}
		}
		//Gdx.gl.glLineWidth(40);

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
	}
}
