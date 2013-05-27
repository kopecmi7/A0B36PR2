/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

/**
 *
 * @author LittleHill
 */
public class GameState {
    boolean isAtive;
    int level;
    String mapName;
    int gameType; //1=levels, 2=created maps, 3=challange
    int gameState; //1=menu, 2=map, 3=start, 4=end
    boolean won;
    String homePath;
    
    public GameState() {
    this.isAtive =false;
    this.level = 0;
    this.mapName = "lvl_0.map";
    this.gameType = 1;
    this.gameState = 1;
    this.won = false;
    this.homePath = "./src/Vlak/";
    }
    
}
