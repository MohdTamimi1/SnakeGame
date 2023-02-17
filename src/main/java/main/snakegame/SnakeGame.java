package main.snakegame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends Application {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private static final int CIRCLE_SIZE = 20;
    private static final int SPEED_INCREMENT = 2;

    private final LinkedList<Circle> snake = new LinkedList<>();
    private Circle fruit;
    private Direction direction;
    private boolean gameOver;
    private int speed;
    private int score;
    private GraphicsContext graphicsContext;

    // to keep timeline in scope and stop it from taking the value of null
    Timeline timeline;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        initialSetup();
        fruit = generateFruit();

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(speed), e -> {
            setGameOver();
            drawCanvas();
            drawSnake();
            drawFruit();
            moveSnake();
        });
        timeline.getKeyFrames().add(frame);
        timeline.play();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Snake Game");
        stage.show();

        // Set up the key handler for reflect user keyboard input
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            } else if (e.getCode() == KeyCode.LEFT && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            } else if (e.getCode() == KeyCode.UP && direction != Direction.DOWN) {
                direction = Direction.UP;
            } else if (e.getCode() == KeyCode.DOWN && direction != Direction.UP) {
                direction = Direction.DOWN;
            }
//            System.out.println(direction);
        });

    }
    public void initialSetup() {
        direction = Direction.RIGHT;
        gameOver = false;
        speed = 100;
        score = 0;

        // Set initial snake size to 3 circles
        for (int i = 0; i < 3; i++) {
            snake.add(new Circle(i, 0));
        }
    }
    public void drawCanvas() {
        graphicsContext.clearRect( 0,0, WIDTH, HEIGHT);
        // Background color
        graphicsContext.setFill(Color.GRAY);
        graphicsContext.fillRect(0, 0, WIDTH, HEIGHT);
    }
    public void drawSnake() {
        // Draw the snake circles
        for (Circle circle : snake) {
//            System.out.println(circle);
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillOval(circle.x() * CIRCLE_SIZE, circle.y() * CIRCLE_SIZE, CIRCLE_SIZE, CIRCLE_SIZE);
//            System.out.println(circle.x() * CIRCLE_SIZE);
            drawStroke(circle);
        }
//        System.out.println(snake);
    }
    public void drawFruit() {
        // Fruit color
        graphicsContext.setFill(Color.CRIMSON);
        graphicsContext.fillOval(fruit.x() * CIRCLE_SIZE, fruit.y() * CIRCLE_SIZE, CIRCLE_SIZE, CIRCLE_SIZE);
        // Fruit circle border
        drawStroke(fruit);
    }
    public void moveSnake() {
        Circle snakeHead = snake.getLast();
        int x = snakeHead.x();
        int y = snakeHead.y();
        switch (direction) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
//        System.out.println(direction);
//        System.out.println(x);

        // x-axis if statements are for the snake to go through the wall and appear on the opposite side
        // Left wall
        if (x < 0)
            x = WIDTH / CIRCLE_SIZE - 1;

        // Right wall
        if (x == WIDTH / CIRCLE_SIZE)
            x = 0;

        // y-axis if statements are for the snake to go through the wall and appear on the opposite side
        // Top
        if (y < 0)
            y = HEIGHT / CIRCLE_SIZE - 1;

        // Bottom
        if (y == HEIGHT / CIRCLE_SIZE)
            y = 0;

        Circle newCircle = new Circle(x, y);
//        System.out.println(newCircle);
        // Check if the snake has collided with its own body
        if (!snake.contains(newCircle)) {
            snake.addLast(newCircle);
            // Check if the snake has eaten the fruit
            if (snakeHead.equals(fruit)) {
                fruit = generateFruit();
                score++;
                // Increase the speed
                if (speed > 0) {
                    speed -= SPEED_INCREMENT;
                } else {
                    speed = 0;
                }
//                System.out.println(speed);
            } else {
                snake.removeFirst();
            }
        } else {
            gameOver = true;
//            System.out.println(snake);
        }
    }
    public void setGameOver() {
        if (gameOver) {
            timeline.stop();

            // Score alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your Final Score: " + score);
            alert.setTitle("Game Over!!!");
            alert.setHeaderText("");
            alert.show();
        }
    }
    public void drawStroke(Circle circleBorder) {
        // Border color and border width
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.setLineWidth(1);

        // Draw the circle
        graphicsContext.strokeOval(circleBorder.x() * CIRCLE_SIZE, circleBorder.y() * CIRCLE_SIZE, CIRCLE_SIZE, CIRCLE_SIZE);
    }
    public Circle generateFruit() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / CIRCLE_SIZE - 1);
//        System.out.println(x);
        int y = random.nextInt(HEIGHT / CIRCLE_SIZE - 1);
//        System.out.println(y);
        return new Circle(x, y);
    }
}