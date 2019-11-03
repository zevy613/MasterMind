import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.text.*;
public class MasterMindBoard extends Application
{
    HBox mainPane; // holds the pallet of colors, the board and the check button
    private int numOfColors; // how many colors you want to play with. only 8 possible
    private int numOfTries; //how many tries would you like to have to guess the code
    Color [] myColors = {Color.CRIMSON, Color.GREEN, Color.BLUE, Color.ORANGE, Color.PURPLE, Color.YELLOW, Color.BROWN, Color.PINK, Color.AQUA, Color.AQUAMARINE, Color.CHARTREUSE, Color.CHOCOLATE};//list of colors
    Circle [] actualCircles;// after choosing the number of colors, all of them get displayed in the pallet.
    int selectedCircle = 0;// the circle that is currently being used

    Circle [] [] attemps; //the board where the colored circles are added. The rows are the number of tries and the colunms will always be four.    
    
    Circle [] responsaCircles; // the marks that get graded from the computer
    
    Circle [] code = new Circle[4]; // holds the code

    Button checkMyGuess; // the button that dignals the computer to grade the players guess
    private int numberOfClicks = 0; // how many times you've clicked the button
    
    Stage solution; // the stage where the solution is displayed
    Text endOfGameMessage = new Text(); // prints winner or looser depending on if the player won or not
    Stage primaryStage = new Stage();

    @Override
    public void start(Stage primaryStage2)
    {
        mainPane = new HBox(50);
                
        primaryStage = new Stage();        

        Scene mainScene = new Scene(mainPane, 1000, 650);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Master Mind");                
        
        gameInput();        

        mainPane.setOnKeyPressed(e ->
            {
                if (e.getCode() == KeyCode.F)
                showCode();
                if (e.getCode() == KeyCode.ESCAPE)
                Platform.exit();
                if (e.getCode() == KeyCode.R)
                reset();
            }                
        );                
    }   
    public void gameInput()
    {
      Stage first = new Stage();
      first.setTitle(" Colors and Tries ");
      FlowPane begin = new FlowPane();
      begin.setAlignment(Pos.CENTER);
      begin.setHgap(25);  
      begin.setVgap(25);
      HBox colorsPlayingWith = new HBox();
      HBox triesPlayingWith = new HBox();

      TextField colorTextField = new TextField();
      TextField triesTextField = new TextField();
      colorTextField.setMaxWidth(30);
      triesTextField.setMaxWidth(30);

      Label colorsLabel = new Label(" How many colors do you want to play with ", colorTextField);
      Label triesLabel = new Label(" How many tries would you like ", triesTextField);
      colorsLabel.setContentDisplay(ContentDisplay.RIGHT); 
      triesLabel.setContentDisplay(ContentDisplay.RIGHT);

      colorsPlayingWith.getChildren().add(colorsLabel);
      triesPlayingWith.getChildren().add(triesLabel);

      begin.getChildren().addAll(colorsPlayingWith, triesPlayingWith);
    
      Button sub = new Button("Submit");
      FlowPane buttonFlowPane = new FlowPane();
      buttonFlowPane.setAlignment(Pos.CENTER);
      buttonFlowPane.getChildren().add(sub);
      begin.getChildren().add(buttonFlowPane);
      
      Scene firstly = new Scene(begin, 400, 400);
      first.setScene(firstly);
      
      first.show();
      sub.setOnMouseClicked(e ->
        {
            setNumOfColors(Integer.parseInt(colorTextField.getText()));
            setNumOfTries(Integer.parseInt(triesTextField.getText()));
            first.close();    
            
            setCode();         
            setUpPallet();
            setUpBoard();
            checkButton();
            primaryStage.show();        
        }      
      );
      
      firstly.setOnKeyPressed( e ->
            {
               if (e.getCode() == KeyCode.ESCAPE)
                Platform.exit();
            }
      );
    }
    public void setUpPallet()
    {
        actualCircles = new Circle[getNumOfColors()];
        HBox palletContainer = new HBox();
        palletContainer.setPadding(new Insets(30)); 
        palletContainer.setMaxHeight(400);
        palletContainer.setPrefWidth(400);
        palletContainer.setStyle("-fx-background-color:grey");
        
        mainPane.getChildren().add(palletContainer);        
        
        BorderPane pallet = new BorderPane();
        pallet.setMaxHeight(250);
        pallet.setMaxWidth(300);
        pallet.setStyle("-fx-background-color:tan");

        palletContainer.getChildren().add(pallet);

        FlowPane header = new FlowPane();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        
        pallet.setTop(header);
        
        Text colorsToChooseFrom = new Text(" choices of colored cirlces ");
        header.getChildren().add(colorsToChooseFrom);
        
        HBox circles = new HBox(10);
        circles.setPrefSize(50,50);
        pallet.setCenter(circles);
        circles.setStyle("-fx-background-color:skyblue");
        
        for (int i = 0; i < getNumOfColors(); i++)
         {
          actualCircles[i] = new Circle(20, myColors[i]);
          circles.getChildren().add(actualCircles[i]);
          final int Q = i;
          //if () only if mouse clicks == the row of the circle clicked
            actualCircles[i].setOnMouseClicked(e ->
             {               
              setSelectedCirlce(Q);
             }          
            );
          
         }
    }
    public void checkButton()
    {
        checkMyGuess = new Button("check my guess");
        checkMyGuess.setMinHeight(100);
        checkMyGuess.setMinWidth(140);
        mainPane.getChildren().add(checkMyGuess);

        checkMyGuess.setOnAction(e->
            {
                setResponsaCircles();
                increaseNumberOfClicks();
            }
        );
    }
    public void setUpBoard()
    {
        
        HBox boardContainer = new HBox();
        boardContainer.setStyle("-fx-background-color:grey");
        boardContainer.setMaxHeight(900);
        boardContainer.setPrefWidth(400);
        mainPane.getChildren().add(boardContainer);
        
        VBox responsa = new VBox(15);        
        responsa.setMinWidth(50);
        
        GridPane [] boxes = new GridPane[getNumOfTries()];
        for (int i = 0; i < boxes.length; i++)
        {                    
            boxes[i] = new GridPane();
            boxes[i].setMinHeight(50);
            boxes[i].setHgap(5);
            boxes[i].setVgap(5);
            responsa.getChildren().add(boxes[i]);
            //boxes[i].setStyle("-fx-border-color:yellow");
        }

        responsaCircles = new Circle[4*getNumOfTries()];
        
        for (int i = 0; i < responsaCircles.length; i++)
        responsaCircles[i] = new Circle(10, Color.BLACK);

        for (int i = 0; i < boxes.length; i++)
        {          
          boxes[i].add(responsaCircles[i*4], 0, 0);
          responsaCircles[i*4].radiusProperty().bind(boxes[i].heightProperty().divide(5));
          boxes[i].add(responsaCircles[i*4+1], 1, 0);        
          responsaCircles[i*4+1].radiusProperty().bind(boxes[i].heightProperty().divide(5));
          boxes[i].add(responsaCircles[i*4+2], 0, 1);        
          responsaCircles[i*4+2].radiusProperty().bind(boxes[i].heightProperty().divide(5));
          boxes[i].add(responsaCircles[i*4+3], 1, 1);
          responsaCircles[i*4+3].radiusProperty().bind(boxes[i].heightProperty().divide(5));
        }
        
        VBox tries = new VBox(28);
        tries.setMinWidth(300);
        attemps = new Circle[getNumOfTries()][4];
        for (int i = 0; i < attemps.length; i++)
        {
            for(int j = 0 ; j < attemps[i].length; j++)
            {
                attemps[i][j] = new Circle(20, Color.BLACK);
                //tries binding   ---          responsaCircles[i*4+3].radiusProperty().bind(boxes[i].heightProperty().divide(5));
                 attemps[i][j].radiusProperty().bind(tries.heightProperty().divide(35));
            }            
        }        

        boardContainer.getChildren().addAll(responsa, tries);
        FlowPane [] spesificTry = new FlowPane[getNumOfTries()];                

        for (int i = 0; i < attemps.length; i++)
        {
           spesificTry[i] = new FlowPane();
           //spesificTry[i].setStyle("-fx-border-color:red");
           spesificTry[i].setAlignment(Pos.CENTER);
           spesificTry[i].setHgap(50);
           for (int j = 0; j < attemps[i].length; j++)
           {
             spesificTry[i].getChildren().add(attemps[i][j]);
             final int I = i;
             final int J = j;
             attemps[i][j].setOnMouseClicked(e->
                {
                    setMyGuess(I, J);
                }
             );
           } 
           tries.getChildren().add(spesificTry[i]);
        }
    }
    
    public void setCode()
    {                
        for (int i = 0; i < code.length; i++)
        code[i] = (new Circle(20, myColors[(int)(Math.random()*getNumOfColors())]));  

        endOfGame();              
    }
    
    public void endOfGame()
    {
        endOfGameMessage.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.ITALIC, 20));
        
        solution = new Stage();
        solution.setTitle("The Soltion Is");
        
        VBox answer = new VBox(35);
        answer.setPadding(new Insets(50,15,50,15));

        FlowPane codeHolder = new FlowPane();
        codeHolder.setHgap(15);
        codeHolder.setAlignment(Pos.CENTER);
        FlowPane buttonHolder = new FlowPane();
        buttonHolder.setAlignment(Pos.CENTER);
        Button resetGame = new Button("Click here if you would like to play agian.");
        buttonHolder.getChildren().add(resetGame);
        
        FlowPane messageHolder = new FlowPane();
        messageHolder.setAlignment(Pos.CENTER);
        messageHolder.getChildren().add(endOfGameMessage); 

        answer.getChildren().addAll(messageHolder, codeHolder, buttonHolder);        
                       
        for (int i = 0; i < code.length; i++)
        {
            codeHolder.getChildren().add(code[i]);
        }                                

         resetGame.setOnMouseClicked(e ->
            {
               reset();   
            }
         );

        Scene finalScene = new Scene(answer, 600, 300);        
        solution.setScene(finalScene);
        finalScene.setOnKeyPressed(e ->
            {                
                if (e.getCode() == KeyCode.ESCAPE)
                Platform.exit();
            }                
        );        
    }
    
    public void showCode()
    {        
        solution.show();        
    }     
    
    public void setResponsaCircles()
    {
            int reds = 0;
            int whites = 0;
            String [] codeStringForm = new String[4];
            String [] guessStringForm = new String[4];
            for (int i = 0; i < 4; i++)
            {
                codeStringForm[i] = code[i].getFill()+"";
                guessStringForm[i] = attemps[getNumberOfClicks()][i].getFill()+"";
                if (codeStringForm[i].equals(guessStringForm[i]))
                {
                   reds++;
                   codeStringForm[i] = "cat";
                   guessStringForm[i] = "cat1";
                }
            }
         
            for (int i = 0; i < 4; i++)
            {
                if (guessStringForm[i] != null)
                {
                   for (int j = 0; j < 4; j++)
                   {
                      if (guessStringForm[i].equals(codeStringForm[j]))
                      {
                         whites++;
                         codeStringForm[j] = "cat";
                         guessStringForm[i] = "cat1";
                      }
                   }            
                }
            }
                   
        if ((getNumberOfClicks() == getNumOfTries()-1) && (reds!= 4))
        {
            endOfGameMessage.setText(" Sorry, You've lost! The code was; ");
            checkMyGuess.setDisable(true);
            showCode();
        }
            setResponsaCircles(reds, whites);             
    }    
    public void setResponsaCircles(int reds, int whites)
    {        
        int i = 0;
        for (; i < reds; i++ ) 
        {
            responsaCircles[(getNumberOfClicks()*4)+i].setFill(Color.RED);
        }
        whites = whites+i;
        for (; i < whites; i++ ) 
        {
            responsaCircles[(getNumberOfClicks()*4)+i].setFill(Color.WHITE);
        }
        
        if (reds == 4)
        {
           endOfGameMessage.setText(" Congradulations! You've become the Master Mind! ");
           showCode();
           checkMyGuess.setDisable(true);
        }        
    }

    public void setMyGuess(int i, int j)
    {
      attemps[i][j].setFill(myColors[selectedCircle]);  
    }    

    public int getSelectedCirlce()
    {
        return(selectedCircle);    
    }
    public void setSelectedCirlce(int x)
    {      
       actualCircles[getSelectedCirlce()].setRadius(20);
       actualCircles[x].setRadius(25);
       selectedCircle = x;            
    }
    public void reset()
    {
        solution.close();
        checkMyGuess.setDisable(false);
        setNumberOfClicks(0);
        setCode();
        for (int i = 0; i < attemps.length; i++)
        {
            for (int j = 0; j < attemps[i].length; j++)
            {
                attemps[i][j].setFill(Color.BLACK);                
            }
        }
        for (int i = 0; i < responsaCircles.length; i++)
        {
            responsaCircles[i].setFill(Color.BLACK);
        }
    }
    
    public void setNumOfColors(int x)
    {
      numOfColors = x;
    }
    public int getNumOfColors()
    {
      return(numOfColors);
    }
    public void setNumOfTries(int x)
    {
      numOfTries = x;
    }
    public int getNumOfTries()
    {
      return(numOfTries);
    }
    public void setNumberOfClicks(int x)
    {
      numberOfClicks = x;    
    }
    public void increaseNumberOfClicks()
    {
      numberOfClicks++;
    }
    public int getNumberOfClicks()
    {
      return(numberOfClicks);    
    }
   
    public static void main(String [] args )
    {
      Application.launch(args);
    }
}