import java.util.*;

class VideoGame {
    String title;
    String genre;
    int id;
    VideoGame next;
}

public class VideoGameLibrary {
    private static VideoGame libraryStart = null;
    private static final Scanner scanner = new Scanner(System.in);

    // Stack to manage removed games
    private static final Stack<VideoGame> removedGames = new Stack<>();

    // Queue to handle recently added games
    private static final Queue<VideoGame> recentGames = new LinkedList<>();


    public static void main(String[] args) {
        libraryStart = initializeLibrary();
        greetings();
        mainMenu();
    }

    private static void greetings() {
        System.out.println("\n\n");
        System.out.println("\t\t\t     ****************************************");
        System.out.println("\t\t\t     *                                      *");
        System.out.println("\t\t\t     *       WELCOME TO VIDEO GAME LIBRARY *");
        System.out.println("\t\t\t     *                                      *");
        System.out.println("\t\t\t     ****************************************");
        System.out.println("\n\t\t\t             Press Enter to continue: ");
        scanner.nextLine();
    }

    private static void mainMenu() {
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
            System.out.println("\t\t\t\t     8. EXIT");
            System.out.println("\t\t\t*************************************************");
            System.out.print("\t\t\t\t      Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> libraryStart = addGame(libraryStart);
                case 2 -> libraryStart = removeGame(libraryStart);
                case 3 -> displayGames(libraryStart);
                case 4 -> displayRecentGames();
                case 5 -> libraryStart = sortGamesByTitle(libraryStart);
                case 6 -> libraryStart = sortGamesById(libraryStart);
                case 7 -> inGenre(libraryStart);
                case 8 -> System.exit(0);
                default -> System.out.println("\n\t\t\t\t      ...Invalid Option!...\n");
            }
        } while (choice != 8);
    }

    private static VideoGame initializeLibrary() {
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
        return game1;
    }

    private static VideoGame addGame(VideoGame start) {
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
                break;  // Exit the loop if no duplicate title is found
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
    
            // Check if ID already exists in the library
            VideoGame ptr = start;
            while (ptr != null) {
                if (ptr.id == newGame.id) {
                    uniqueId = false;
                    System.out.println("\n\t ID already exists! Please enter a unique ID.");
                    break;
                }
                ptr = ptr.next;
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
    
        // Add to the recent games queue
        recentGames.add(newGame);
        if (recentGames.size() > 5) {
            recentGames.poll(); // Keep only the last 5 added games
        }
    
        System.out.println("\n\t Game added successfully!\n");
        return start;
    }
    

    private static VideoGame removeGame(VideoGame start) {
        System.out.print("\n\n\t Enter the Game ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        VideoGame ptr = start;
        VideoGame prev = null;
        boolean found = false;

        while (ptr != null) {
            if (ptr.id == id) {
                found = true;
                break;
            }
            prev = ptr;
            ptr = ptr.next;
        }

        if (!found) {
            System.out.println("\n\t Game with the given ID not found!\n");
            return start;
        }

        if (prev == null) {
            start = ptr.next;
        } else {
            prev.next = ptr.next;
        }

        removedGames.push(ptr);
        System.out.println("\n\t Game removed successfully!\n");
        return start;
    }

    private static void displayGames(VideoGame start) {
        if (start == null) {
            System.out.println("\n\t No games in the library.\n");
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
    private static void displayRecentGames() {
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
    
    private static VideoGame sortGamesByTitle(VideoGame start) {
        if (start == null || start.next == null) {
            return start;
        }

        // Convert the linked list to an ArrayList for sorting
        List<VideoGame> gameList = new ArrayList<>();
        VideoGame ptr = start;
        while (ptr != null) {
            gameList.add(ptr);
            ptr = ptr.next;
        }

        // Sort the games alphabetically by title
        gameList.sort(Comparator.comparing(game -> game.title.toLowerCase()));

        // Reconstruct the linked list
        VideoGame newStart = gameList.get(0);
        VideoGame current = newStart;

        for (int i = 1; i < gameList.size(); i++) {
            current.next = gameList.get(i);
            current = current.next;
        }
        current.next = null;

        System.out.println("\n\t Games sorted by title successfully!\n");
        return newStart;
    }

    private static VideoGame sortGamesById(VideoGame start) {
        if (start == null || start.next == null) {
            return start;
        }
    
        // Convert the linked list to an ArrayList for sorting
        List<VideoGame> gameList = new ArrayList<>();
        VideoGame ptr = start;
        while (ptr != null) {
            gameList.add(ptr);
            ptr = ptr.next;
        }
    
        // Sort the games by ID
        gameList.sort(Comparator.comparingInt(game -> game.id));
    
        // Reconstruct the linked list from the sorted ArrayList
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

    private static void inGenre(VideoGame games)
    {
        System.out.println("Enter a genre");
        String genre = scanner.nextLine();
        List<VideoGame> ingenre = new ArrayList<>();
        VideoGame temp = games;
        while(temp != null)
        {
            if(temp.genre.equals(genre))
            {
                ingenre.add(temp);
            }
            temp = temp.next;
        }
        if(ingenre==null)
        {
            System.out.println("\t No games in library are in that genre");
        }
        else
        {
            System.out.println("\t Games in " + genre + " genre");
            for(VideoGame v:ingenre)
            {
                System.out.println("\t Game Title: " + v.title);
                System.out.println("\t Game ID: " + v.id);
            }
        }
    }
}

