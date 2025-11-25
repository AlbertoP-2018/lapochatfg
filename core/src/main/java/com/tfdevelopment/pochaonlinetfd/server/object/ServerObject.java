package com.tfdevelopment.pochaonlinetfd.server.object;

import com.tfdevelopment.pochaonlinetfd.server.object.game.NewGameDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.news.NewsServer;
import com.tfdevelopment.pochaonlinetfd.server.object.ranking.RankingUserDataServer;
import com.tfdevelopment.pochaonlinetfd.server.object.userdata.UserDataServer;
import com.tfdevelopment.pochaonlinetfd.utils.conf.Config;

import java.util.ArrayList;

public class ServerObject {
    private ArrayList<UserDataServer> alUserDataServer;
    private ArrayList<NewGameDataServer> alNewGameDataServer;
    private String lastUpdateDate;
    private int iRanking, iOrder; //0: Inauguración -- 1: Temporada 1 -- 2: Temporada 2...
    private ArrayList<RankingUserDataServer>[][] alSPRanking; //0: Temporada 0 -- X-1: Temporada X-1 -- X: Torneos --> [Puntos][Ratio]
    private String newsVersion;
    private ArrayList<NewsServer> alNews;

    public ServerObject(String newsVersion){
        this.newsVersion = newsVersion;
        this.alUserDataServer = new ArrayList<>();
        this.alNewGameDataServer = new ArrayList<>();

        //Temporadas (-1 públicas)
        int numRanking = (Config.NUM_SEASONS)+1;
        this.alSPRanking = new ArrayList[numRanking][2];
        for(int i=0; i<numRanking; i++){
            alSPRanking[i][0] = null;
            alSPRanking[i][1] = null;
        }
        this.iRanking = -1;
        this.iOrder = -1;
        this.alNews = new ArrayList<>();
    }

    public ArrayList<RankingUserDataServer> getALRanking(int iRanking, int iOrder){
        if(iRanking==100) return alSPRanking[alSPRanking.length-1][iOrder];
        return alSPRanking[iRanking][iOrder];
    }

    public void setAlSPRanking(int iRanking, int iOrder, String lastRankingUpdate, ArrayList<RankingUserDataServer> al){
        this.iRanking = iRanking;
        this.iOrder = iOrder;
        this.lastUpdateDate = lastRankingUpdate;
        if(iRanking==100) alSPRanking[alSPRanking.length-1][iOrder] = al;
        else alSPRanking[iRanking][iOrder] = al;
    }

    public void addUserDataServer(UserDataServer _userDataServer) {
        if (!alUserDataServer.contains(_userDataServer)) this.alUserDataServer.add(_userDataServer);
    }

    public void deleteUserDataServer(String userName) {
        for (int i=0; i<alUserDataServer.size(); i++) {
            if (alUserDataServer.get(i).getName().equals(userName)) {
                alUserDataServer.remove(i);
                break;
            }
        }
    }

    public void addNewGameDataServer(NewGameDataServer newGameDataServer) {
        this.alNewGameDataServer.add(newGameDataServer);
        for (UserDataServer userDataServer : alUserDataServer) {
            if (userDataServer.getName().equals(newGameDataServer.getHostName())) {
                userDataServer.setGameId(newGameDataServer.getId());
            }
        }
    }

    public void deleteNewGameDataServer(String id) {
        for (int i=0; i<alNewGameDataServer.size(); i++) {
            NewGameDataServer newGameDataServer = alNewGameDataServer.get(i);
            if (newGameDataServer.getId().equals(id)) {
//                for (UserDataServer userDataServer : alUserDataServer) {
//                    if (newGameDataServer.getAlPlayerNames().contains(userDataServer.getName())) {
//                        userDataServer.setGameId("");
//                    }
//                }
                alNewGameDataServer.remove(i);
                break;
            }
        }

        for (UserDataServer userDataServer : alUserDataServer) {
            if (userDataServer.getGameId().equals(id)) {
                userDataServer.setGameId("");
            }
        }
    }

    public void hideStartedGame(String id) {
        for (int i=0; i<alNewGameDataServer.size(); i++) {
            NewGameDataServer newGameDataServer = alNewGameDataServer.get(i);
            if (newGameDataServer.getId().equals(id)) {
                alNewGameDataServer.remove(i);
                break;
            }
        }
    }

    public void addPlayerGame(String id, String playerName) {
        boolean found = false;
        for (NewGameDataServer newGDS : alNewGameDataServer) {
            if (newGDS.getId().equals(id)) {
                newGDS.getAlPlayerNames().add(playerName);
                found = true;
                break;
            }
        }

        if (found) {
            for (UserDataServer userDataServer : alUserDataServer) {
                if (userDataServer.getName().equals(playerName)){
                    userDataServer.setGameId(id);
                }
            }
        }
    }

    public void removePlayerGame(String id, String playerName) {
        boolean found = false;
        for (NewGameDataServer newGDS : alNewGameDataServer) {
            if (newGDS.getId().equals(id)) {
                for (int i = 0; i<newGDS.getAlPlayerNames().size(); i++) {
                    if (newGDS.getAlPlayerNames().get(i).equals(playerName)) {
                        newGDS.getAlPlayerNames().remove(i);
                        found = true;
                        break;
                    }
                }
            }
        }

        if (found) {
            for (UserDataServer userDataServer : alUserDataServer) {
                if (userDataServer.getName().equals(playerName)){
                    userDataServer.setGameId("");
                }
            }
        }
    }

    public void updateUserIcon(String userName, int icon) {
        for (UserDataServer userDataServer : alUserDataServer) {
            if (userDataServer.getName().equals(userName)) {
                userDataServer.setUserIcon(icon);
            }
        }
    }

    public int getLengthRanking(){ return alSPRanking.length; }

    /*** Getters And Setters ***/
    public String getLastUpdateDate(){ return lastUpdateDate; }
    public int getiRanking(){ return iRanking; }
    public void setiRanking(int iRanking){ this.iRanking = iRanking; }
    public int getiOrder(){ return iOrder; }
    public void setiOrder(int iOrder){ this.iOrder = iOrder; }
    public String getNewsVersion() { return newsVersion; }
    public void setNewsVersion(String newsVersion){ this.newsVersion = newsVersion; }
    public ArrayList<NewsServer> getAlNews() { return alNews; }
    public void setAlNews(ArrayList<NewsServer> alNews){ this.alNews = alNews; }
    public void setAlNewGameDataServer(ArrayList<NewGameDataServer> alNewGameDataServer){ this.alNewGameDataServer = alNewGameDataServer; }
    public ArrayList<UserDataServer> getAlServerDataUser() { return alUserDataServer; }
    public void setAlServerDataUser(ArrayList<UserDataServer> alUserDataServer){ this.alUserDataServer = alUserDataServer; }
    public ArrayList<NewGameDataServer> getAlNewGameDataServer() { return alNewGameDataServer; }
}
