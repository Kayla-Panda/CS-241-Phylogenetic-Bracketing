import java.util.*;

class VideoGame {
    String title;
    String genre;
    int id;
    VideoGame next;
}

public class VideoGameLibrary {
    public static VideoGame libraryStart = null;
    public static final Scanner scanner = new Scanner(System.in);

    // Stack to manage removed games
    public static final Stack<VideoGame> removedGames = new Stack<>();

    // Queue to handle recently added games
    public static final Queue<VideoGame> recentGames = new LinkedList<>();

    public static final HashMap<Integer, VideoGame> gameMap = new HashMap<>();

    public static void main(String[] args) {
        libraryStart = initializeLibrary();
        greetings();
        mainMenu();
    }

    public static void greetings() {
        System.out.println("\n\n");
        System.out.println("\t\t\t     ****************************************");
        System.out.println("\t\t\t     *                                      *");
        System.out.println("\t\t\t     *       WELCOME TO VIDEO GAME LIBRARY *");
        System.out.println("\t\t\t     *                                      *");
        System.out.println("\t\t\t     ****************************************");
        System.out.println("\n\t\t\t             Press Enter to continue: ");
        scanner.nextLine();
    }

    public static void mainMenu() {
        int choice;
        do {
            System.out.println("\n\t\t\t*************************************************");
            System.out.println("\t\t\t\t      MAIN MENU:");
            System.out.println("\t\t\t\t     1. ADD NEW GAME");
            System.out.println("\t\t\t\t     2. REMOVE GAME");
            System.out.println("\t\t\t\t     3. DISPLAY ALL GAMES");
            System.out.println("\t\t\t\t     4. DISPLAY RECENTLY ADDED GAMES");
            System.out.println("\t\t\t\t     5. SORT GAMES BY TITLE");
            System.out.println("\t\t\t\t     6. SORT GAMES BY ID");
            System.out.println("\t\t\t\t     7. SEARCH GAMES IN GENRE");
            System.out.println("\t\t\t\t     8. SEARCH GAME BY ID");
            System.out.println("\t\t\t\t     9. EXIT");
            System.out.println("\t\t\t*************************************************");
            System.out.print("\t\t\t\t      Enter your choice: ");
            
            // Check if the user enters a valid integer
            while (!scanner.hasNextInt()) {
                System.out.println("\n\t\t\t\t      Invalid input! Please enter a number between 1 and 9.");
                scanner.nextLine(); // Consume the invalid input
                System.out.print("\t\t\t\t      Enter your choice: ");
            }
            
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
    
            switch (choice) {
                case 1 -> libraryStart = addGame(libraryStart);
                case 2 -> libraryStart = removeGame(libraryStart);
                case 3 -> displayGames(libraryStart);
                case 4 -> displayRecentGames();
                case 5 -> {
                    libraryStart = mergeSortByTitle(libraryStart);
                    System.out.println("\n\t Games sorted by Title successfully!\n");
                }
                case 6 -> libraryStart = sortGamesById(libraryStart);
                case 7 -> inGenre(libraryStart);
                case 8 -> findGameById(); // Return to menu after execution
                case 9 -> {
                    System.out.println("\n\t Exiting the Video Game Library. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("\n\t\t\t\t      ...Invalid Option!...\n");
            }
        } while (true); // Keep showing the menu until the user exits
    }

    public static VideoGame initializeLibrary() {
        VideoGame game1 = new VideoGame();
        game1.title = "The Legend of Zelda: Breath of the Wild";
        game1.genre = "Action-Adventure";
        game1.id = 1;
    
        VideoGame game2 = new VideoGame();
        game2.title = "Elden Ring";
        game2.genre = "RPG";
        game2.id = 2;
    
        VideoGame game3 = new VideoGame();
        game3.title = "Minecraft";
        game3.genre = "Sandbox";
        game3.id = 3;
    
        game1.next = game2;
        game2.next = game3;
    
        // Populate the gameMap with the initialized games
        gameMap.put(game1.id, game1);
        gameMap.put(game2.id, game2);
        gameMap.put(game3.id, game3);
    
        return game1;
    }

    public static VideoGame addGame(VideoGame start) {
        VideoGame newGame = new VideoGame();
        System.out.print("\n\t Enter Game Title: ");
        newGame.title = scanner.nextLine();
    
        // Check for duplicate title
        boolean duplicateTitle;
        do {
            duplicateTitle = false;
            VideoGame ptr = start;
            while (ptr != null) {
                if (ptr.title.equalsIgnoreCase(newGame.title)) {
                    duplicateTitle = true;
                    System.out.println("\n\t A game with this title already exists! Please enter a different title.");
                    break;
                }
                ptr = ptr.next;
            }
            if (!duplicateTitle) {
                break;
            }
            System.out.print("\n\t Enter Game Title: ");
            newGame.title = scanner.nextLine();
        } while (duplicateTitle);
    
        System.out.print("\n\t Enter Game Genre: ");
        newGame.genre = scanner.nextLine();
    
        // Ensure ID is unique
        boolean uniqueId;
        do {
            uniqueId = true;
            System.out.print("\n\t Enter Game ID: ");
            newGame.id = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
    
            if (gameMap.containsKey(newGame.id)) {
                uniqueId = false;
                System.out.println("\n\t ID already exists! Please enter a unique ID.");
            }
        } while (!uniqueId);
    
        // Add the new game to the library
        if (start == null) {
            start = newGame;
        } else {
            VideoGame temp = start;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newGame;
        }
    
        // Update the HashMap
        gameMap.put(newGame.id, newGame);
    
        // Add to the recent games queue
        recentGames.add(newGame);
        if (recentGames.size() > 5) {
            recentGames.poll(); // Keep only the last 5 added games
        }
    
        System.out.println("\n\t Game added successfully!\n");
        return start;
    }    

    public static VideoGame removeGame(VideoGame start) {
        if (start == null) {
            System.out.println("\n\t The library is empty. No games to remove.\n");
            return null;
        }
    
        System.out.print("\n\n\t Enter the Game ID to remove: ");
        if (!scanner.hasNextInt()) {
            System.out.println("\n\t Invalid input! Please enter a numeric ID.");
            scanner.nextLine(); // Consume invalid input
            return start;
        }
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
    
        if (!gameMap.containsKey(id)) {
            System.out.println("\n\t Game with the given ID not found!\n");
            return start;
        }
    
        VideoGame removed = gameMap.remove(id);
        if (start == removed) {
            start = start.next;
        } else {
            VideoGame current = start;
            while (current.next != removed) {
                current = current.next;
            }
            current.next = removed.next;
        }
    
        removedGames.push(removed);
        System.out.println("\n\t Game removed successfully!\n");
        return start;
    }

    // Add a method to quickly find a game by its ID 
    public static void findGameById() {
        if (libraryStart == null) {
            System.out.println("\n\t The library is empty. No games to search for.\n");
            return;
        }
    
        System.out.print("\n\n\t Enter the Game ID to find: ");
        if (!scanner.hasNextInt()) {
            System.out.println("\n\t Invalid input! Please enter a numeric ID.");
            scanner.nextLine(); // Consume invalid input
            return;
        }
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
    
        VideoGame game = gameMap.get(id);
        if (game != null) {
            System.out.println("\n\t************* Game "+ id + " found *************");
            System.out.println("\t_________________________________________");
            System.out.println("\t Title: " + game.title);
            System.out.println("\t Genre: " + game.genre);
            System.out.println("\t ID: " + game.id);
            System.out.println("\t_________________________________________");
        } else {
            System.out.println("\n\t Game with the given ID not found!");
        }
    }

    public static void displayGames(VideoGame start) {
        if (start == null) {
            System.out.println("\n\t The library is empty. No games to display.\n");
            return;
        }
    
        VideoGame ptr = start;
        System.out.println("\n\t************* Video Game Library *************\n");
        while (ptr != null) {
            System.out.println("\t_________________________________________");
            System.out.println("\t Game Title: " + ptr.title);
            System.out.println("\t Genre: " + ptr.genre);
            System.out.println("\t Game ID: " + ptr.id);
            System.out.println("\t_________________________________________");
            ptr = ptr.next;
        }
    }
    public static void displayRecentGames() {
        if (recentGames.isEmpty()) {
            System.out.println("\n\t No recently added games to display.\n");
            return;
        }

        System.out.println("\n\t************* Recently Added Games *************\n");
        for (VideoGame game : recentGames) {
            System.out.println("\t_________________________________________");
            System.out.println("\t Game Title: " + game.title);
            System.out.println("\t Genre: " + game.genre);
            System.out.println("\t Game ID: " + game.id);
            System.out.println("\t_________________________________________");
        }
    }
    
    public static VideoGame mergeSortByTitle(VideoGame head) {
        if (head == null || head.next == null) {
            return head; // Already sorted
        }
    
        VideoGame middle = getMiddle(head);
        VideoGame nextOfMiddle = middle.next;
        middle.next = null;
    
        VideoGame left = mergeSortByTitle(head);
        VideoGame right = mergeSortByTitle(nextOfMiddle);
    
        return mergeByTitle(left, right);
    }    

    public static VideoGame getMiddle(VideoGame head) { 
        if (head == null) { 
            return head; 
        }
         
        VideoGame slow = head, fast = head; 
        while (fast.next != null && fast.next.next != null) { 
            slow = slow.next; fast = fast.next.next; 
        } 
        return slow; 
    }

    public static VideoGame mergeByTitle(VideoGame left, VideoGame right) { 
        VideoGame result = null; 
        if (left == null) return right; 
        if (right == null) return left; 

        if (left.title.compareToIgnoreCase(right.title) <= 0) { 
            result = left; result.next = mergeByTitle(left.next, right); 
        } 
        else { 
            result = right; 
            result.next = mergeByTitle(left, right.next); 
        } 
        return result; 
    }

    public static VideoGame sortGamesById(VideoGame start) {
        if (start == null || start.next == null) {
            System.out.println("\n\t The library is empty or contains only one game. Sorting not required.\n");
            return start;
        }
    
        List<VideoGame> gameList = new ArrayList<>();
        VideoGame ptr = start;
        while (ptr != null) {
            gameList.add(ptr);
            ptr = ptr.next;
        }
    
        gameList.sort(Comparator.comparingInt(game -> game.id));
    
        VideoGame newStart = gameList.get(0);
        VideoGame current = newStart;
    
        for (int i = 1; i < gameList.size(); i++) {
            current.next = gameList.get(i);
            current = current.next;
        }
        current.next = null;
    
        System.out.println("\n\t Games sorted by ID successfully!\n");
        return newStart;
    }

    public static void inGenre(VideoGame games) {
        if (games == null) {
            System.out.println("\n\t The library is empty. No games to search for.\n");
            return;
        }
    
        System.out.print("\nEnter a genre: ");
        String genre = scanner.nextLine();
        List<VideoGame> ingenre = new ArrayList<>();
        VideoGame temp = games;
    
        while (temp != null) {
            if (temp.genre.equalsIgnoreCase(genre)) {
                ingenre.add(temp);
            }
            temp = temp.next;
        }
    
        if (ingenre.isEmpty()) {
            System.out.println("\tNo games in the library are in that genre.\n");
        } else {
            System.out.println("\n\t************* Games in '" + genre + "' Genre *************\n");
            for (VideoGame v : ingenre) {
                System.out.println("\t_________________________________________");
                System.out.println("\t Game Title: " + v.title);
                System.out.println("\t Genre: " + v.genre);
                System.out.println("\t Game ID: " + v.id);
                System.out.println("\t_________________________________________");
            }
        }
    }
}