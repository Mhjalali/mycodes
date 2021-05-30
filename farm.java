import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class farm {
    static ArrayList<Cow> cows = new ArrayList<>();
    static akhor akhor = new akhor();
    static Tank tank = new Tank();
    static Storage storage = new Storage();
    static int id_counter = 0;
    static int death_counter = 0;
    static ArrayList<Integer> deadcow_id = new ArrayList<>();
    static int[] cowids = new int[500];
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String str = "";
        int flag = 0, flag2 = 0;
        Food barley = new Food("barley", 4, 80, 4);
        Food alfalfa = new Food("alfalfa", 3, 60, 3);
        Food straw = new Food("straw", 2, 20, 0);
        storage.food.add(alfalfa);
        storage.food.add(straw);
        storage.food.add(barley);
        akhor.food.add(alfalfa);
        akhor.food.add(straw);
        akhor.food.add(barley);
        while (true) {
            flag = 0;
            str = input.nextLine();
            if (str.matches("end")) {
                break;
            }
            if (str.matches("day passed")) {
                flag = 1;
                daypassed();
            }
            if (str.matches("status farm")) {
                flag = 1;
                statusfarm();
            }
            if (str.matches("add cow")) {
                flag = 1;
                addcow();
            }
            if (str.matches("add storage \\w+ \\d+")) {
                flag = 1;
                addtostorage(str);
            }
            if (str.matches("status cow \\d+")) {
                flag = 1;
                int idcow = Integer.parseInt(str.substring(str.lastIndexOf("w") + 2));
                showstatuscow(idcow);
            }
            if (str.matches("add new food \\w+ \\d+")) {
                flag = 1;
                int a = Integer.parseInt(input.nextLine());
                int b = Integer.parseInt(input.nextLine());
                int c = Integer.parseInt(input.nextLine());
                addnewfood(str, a, b, c);
            }
            if (str.matches("status storage")) {
                flag = 1;
                storage.showstatus();
            }
            if (str.matches("tank inventory")) {
                flag = 1;
                System.out.println("tank inventory: " + tank.getCapacity() + " liters");
            }
            if (str.matches("butcher cow \\d+")) {
                flag = 1;
                butcher(str);
            }
            if (str.matches("feed")) {
                flag = 1;
                while (true) {
                    String str1 = input.nextLine();
                    if (str1.matches("end_feed")) {
                        feedcows();
                        break;
                    }
                    akhor.addfood(str1);
                }
            }
            if (str.matches("milk cow \\d+")) {
                flag = 1;
                int idcow = Integer.parseInt(str.substring(str.lastIndexOf("w") + 2));
                milkcow(idcow);
            }
            if (str.matches("sell milk \\d+")) {
                flag = 1;
                int m = Integer.parseInt(str.substring(str.lastIndexOf("k") + 2));
                if (m > tank.capacity)
                    System.out.println("there is not enough milk");
                else {
                    System.out.println("milk sold successfully");
                    tank.capacity -= m;
                }
            }
            if (str.matches("show ranks")) {
                flag = 1;
                showranks();
            }
            if (flag == 0)
                System.out.println("invalid command");
        }
    }

    public static void daypassed() {
        for (int i = 0; i < cows.size(); i++) {
            cows.get(i).daychanges();
            if (cows.get(i).isMilky()) {
                if (cows.get(i).milk <= cows.get(i).maxmilk) {
                    tank.capacity += cows.get(i).milk;
                    cows.get(i).milkproduced += cows.get(i).milk;
                    cows.get(i).milk = 0;
                } else {
                    tank.capacity += cows.get(i).maxmilk;
                    cows.get(i).milkproduced += cows.get(i).maxmilk;
                    cows.get(i).milk -= cows.get(i).maxmilk;
                }
            }
        }
        for (int i = 0; i < cows.size(); i++) {
            cows.get(i).milk = 0;
        }
        feedcows();
        for (int i = 0; i < id_counter; ++i) {
            if (cowfinder(cowids[i]) != -1) {
                if (cows.get(cowfinder(cowids[i])).hunger >= 4 * cows.get(cowfinder(cowids[i])).food || cows.get(cowfinder(cowids[i])).weight < 100) {
                    deadcow_id.add(cows.get(cowfinder(cowids[i])).getId());
                    death_counter++;
                    cows.remove(cowfinder(cowids[i]));
                }
            }
        }
    }

    public static void showstatuscow(int idcow) {
        if (cowfinder(idcow) == -1) {
            System.out.println("invalid cow");
            return;
        }
        System.out.println("cow " + idcow);
        System.out.println("age: " + cows.get(cowfinder(idcow)).age);
        System.out.println("hunger: " + cows.get(cowfinder(idcow)).hunger);
        System.out.println("weight: " + cows.get(cowfinder(idcow)).weight);
        System.out.println("milk: " + cows.get(cowfinder(idcow)).milk);
        System.out.println("milk produced: " + cows.get(cowfinder(idcow)).milkproduced);
    }

    public static void addtostorage(String str) {
        String[] str0 = str.split(" ");
        int amount = Integer.parseInt(str0[3]);
        String foodname = str0[2];
        for (int i = 0; i < storage.food.size(); ++i) {
            if (foodname.contentEquals(storage.food.get(i).name)) {
                storage.food.get(i).amount_storage += amount;
                return;
            }
        }
        System.out.println("invalid food name");
    }

    public static void addcow() {
        if (death_counter > 0) {
            death_counter--;
            System.out.println("cow added. cow num: " + deadcow_id.get(0));
            Cow cow1 = new Cow(deadcow_id.get(0));
            cows.add(cow1);
            deadcow_id.remove(0);
        } else {
            id_counter++;
            System.out.println("cow added. cow num: " + id_counter);
            cowids[id_counter - 1] = id_counter;
            Cow cow = new Cow(id_counter);
            cows.add(cow);
        }
    }

    public static int cowfinder(int idcow) {
        for (int i = 0; i < cows.size(); ++i) {
            if (cows.get(i).getId() == idcow)
                return i;
        }
        return -1;
    }

    public static void statusfarm() {
        System.out.println("dairy farm");
        System.out.println("number of cows: " + cows.size());
        System.out.print("cows:");
        Arrays.sort(cowids, 0, id_counter);
        for (int i = 0; i < id_counter; ++i) {
            if (cowfinder(cowids[i]) != -1)
                System.out.print(" " + cows.get(cowfinder(cowids[i])).getId());
        }
        System.out.println();
        System.out.println("the remaining food:");
        storage.showstatus();
        System.out.println("tank inventory: " + tank.getCapacity() + " liters");
        System.out.print("max milk production: ");
        int sum = 0;
        for (int i = 0; i < cows.size(); ++i) {
            sum += cows.get(i).maxmilk;
        }
        System.out.println(sum);
    }

    public static void addnewfood(String str, int a, int b, int c) {
        String[] str0 = str.split(" ");
        int amount = Integer.parseInt(str0[4]);
        Food food = new Food(str0[3], a, c, b, amount);
        storage.food.add(food);
        akhor.food.add(food);
    }

    public static void butcher(String str) {
        int idcow = Integer.parseInt(str.substring(str.lastIndexOf("w") + 2));
        if (cowfinder(idcow) == -1) {
            System.out.println("invalid cow");
            return;
        } else {
            System.out.println("cow butchered successfully");
            deadcow_id.add(cows.get(cowfinder(idcow)).getId());
            death_counter++;
            cows.remove(cowfinder(idcow));

        }
    }

    public static void sort_cows_for_food() {
        for (int i = 0; i < cows.size() - 1; i++) {
            for (int j = 0; j < cows.size() - 1; j++) {
                if (cows.get(j).hunger < cows.get(j + 1).hunger) {
                    Collections.swap(cows, j, j + 1);
                }
                if (cows.get(j).hunger == cows.get(j + 1).hunger) {
                    if (cows.get(j).age > cows.get(j + 1).age) {
                        Collections.swap(cows, j, j + 1);
                    }
                    if (cows.get(j).age == cows.get(j + 1).age) {
                        if (cows.get(j).getId() > cows.get(j + 1).getId()) {
                            Collections.swap(cows, j, j + 1);
                        }
                    }
                }
            }
        }
    }

    public static void feedcows() {
        sort_cows_for_food();
        akhor.sort();
        int flag = 0;
        int k = 0;
        int k1 = 0;
        for (int i = 0; i < akhor.food.size(); i++) {
            if (k == cows.size()) {
                k1 = 0;
            } else {
                k1 = k;
            }
            int amount = akhor.food.get(akhor.foodfinder_favorite_rating(akhor.favratings[akhor.food.size() - 1 - i])).amount_akhor;
            int milkproduced = akhor.food.get(akhor.foodfinder_favorite_rating(akhor.favratings[akhor.food.size() - 1 - i])).milk_produced;
            int weightadded = akhor.food.get(akhor.foodfinder_favorite_rating(akhor.favratings[akhor.food.size() - 1 - i])).weight_added;
            while (amount > 0) {
                if (k == cows.size())
                    k1 = 0;
                flag = 0;
                for (k = k1; k < cows.size(); k++) {
                    if (amount <= 0)
                        break;
                    if (cows.get(k).hunger > 0) {
                        flag = 1;
                        amount--;
                        akhor.food.get(akhor.foodfinder_favorite_rating(akhor.favratings[akhor.food.size() - 1 - i])).amount_akhor--;
                        // System.out.println(cows.get(k).getId());
                        cows.get(k).hunger--;
                        cows.get(k).milk += milkproduced;
                        cows.get(k).weight += weightadded;
                    }
                }
                if (flag == 0)
                    break;
            }
        }
    }

    public static void milkcow(int idcow) {
        if (cowfinder(idcow) == -1) {
            System.out.println("invalid cow");
            return;
        }
        if (cows.get(cowfinder(idcow)).milk <= 0) {
            System.out.println("cow has no milk");
            return;
        }
        if (cows.get(cowfinder(idcow)).milk <= cows.get(cowfinder(idcow)).maxmilk) {
            System.out.println("cow milked successfully");
            tank.capacity += cows.get(cowfinder(idcow)).milk;
            cows.get(cowfinder(idcow)).milkproduced += cows.get(cowfinder(idcow)).milk;
            cows.get(cowfinder(idcow)).milk = 0;
            return;
        } else {
            System.out.println("cow milked successfully");
            tank.capacity += cows.get(cowfinder(idcow)).maxmilk;
            cows.get(cowfinder(idcow)).milkproduced += cows.get(cowfinder(idcow)).maxmilk;
            cows.get(cowfinder(idcow)).milk -= cows.get(cowfinder(idcow)).maxmilk;
            return;
        }
    }

    public static void showranks() {
        for (int i = 0; i < cows.size() - 1; i++) {
            for (int j = 0; j < cows.size() - 1; j++) {
                if (cows.get(j).milkproduced < cows.get(j + 1).milkproduced) {
                    Collections.swap(cows, j, j + 1);
                }
                if (cows.get(j).milkproduced == cows.get(j + 1).milkproduced) {
                    if (cows.get(j).getId() > cows.get(j + 1).getId()) {
                        Collections.swap(cows, j, j + 1);
                    }
                }
            }
        }
        for (int i = 0; i < cows.size(); i++) {
            System.out.println("cow " + cows.get(i).getId() + ": " + cows.get(i).milkproduced + " liters");
        }
    }
}

class Food {
    String name;
    int milk_produced;
    int favorite_rating;
    int weight_added;
    int amount_storage;
    int amount_akhor;

    public Food(String name, int milk_produced, int favorite_rating, int weight_added, int amount_storage) {
        this.name = name;
        this.milk_produced = milk_produced;
        this.favorite_rating = favorite_rating;
        this.weight_added = weight_added;
        this.amount_storage = amount_storage;
    }

    public Food(String name, int milk_produced, int favorite_rating, int weight_added) {
        this.name = name;
        this.milk_produced = milk_produced;
        this.favorite_rating = favorite_rating;
        this.weight_added = weight_added;
    }

    public Food(String name, int milk_produced, int favorite_rating, int weight_added, int amount_storage, int amount_akhor) {
        this.name = name;
        this.milk_produced = milk_produced;
        this.favorite_rating = favorite_rating;
        this.weight_added = weight_added;
        this.amount_storage = amount_storage;
        this.amount_akhor = amount_akhor;
    }
}

class akhor {
    ArrayList<Food> food = new ArrayList<>();
    int favratings[] = new int[50];

    public void sort() {
        for (int i = 0; i < food.size(); ++i) {
            favratings[i] = food.get(i).favorite_rating;
        }
        Arrays.sort(favratings, 0, food.size());
    }

    public int foodfinder_favorite_rating(int favorite_rating) {
        for (int i = 0; i < food.size(); ++i) {
            if (food.get(i).favorite_rating == favorite_rating) {
                return i;
            }
        }
        return 0;
    }

    public void addfood(String str) {
        String[] str0 = str.split(" ");
        int amount = Integer.parseInt(str0[1]);
        for (int i = 0; i < farm.storage.food.size(); ++i) {
            if (str0[0].contentEquals(farm.storage.food.get(i).name)) {
                if (amount <= farm.storage.food.get(i).amount_storage) {
                    farm.storage.food.get(i).amount_storage -= amount;
                    for (int j = 0; j < food.size(); j++) {
                        if (str0[0].contentEquals(food.get(j).name))
                            food.get(j).amount_akhor += amount;
                    }
                }
            }
        }
    }
}

class Cow {
    int age = 0;
    int ten_days = 0;
    int three_days = 0;
    private int id;
    int weight = 200;
    int food = 5;
    int milk = 0;
    int hunger = 0;
    int milkproduced = 0;
    int maxmilk = 25;
    private boolean deathstatus = false;
    private boolean milky = true;

    public void daychanges() {
        age++;
        ten_days++;
        if (milk == 0)
            three_days++;
        if (age % 10 == 0) {
            food++;
            maxmilk = maxmilk + 5;
            ten_days = 0;
        }
        if (three_days == 3) {
            milky = false;
        }
        if (hunger > 0)
            weight -= food;
        hunger += food;
    }

    public Cow(int id) {
        this.id = id;
    }

    public void setDeathstatus(boolean deathstatus) {
        this.deathstatus = deathstatus;
    }

    public int getId() {
        return id;
    }

    public boolean isMilky() {
        return milky;
    }

}

class Storage {
    ArrayList<Food> food = new ArrayList<>();


    public void showstatus() {
        int favratings[] = new int[50];
        for (int i = 0; i < food.size(); ++i) {
            favratings[i] = food.get(i).favorite_rating;
        }

        Arrays.sort(favratings, 0, food.size());
        for (int i = food.size() - 1; i >= 0; --i) {
            if (food.get(foodfinder_favorite_rating(favratings[i])).amount_storage == 0)
                continue;
            System.out.println(food.get(foodfinder_favorite_rating(favratings[i])).name + " " + food.get(foodfinder_favorite_rating(favratings[i])).amount_storage);
        }
    }

    public int foodfinder_favorite_rating(int favorite_rating) {
        for (int i = 0; i < food.size(); ++i) {
            if (food.get(i).favorite_rating == favorite_rating) {
                return i;
            }
        }
        return 0;
    }
}

class Tank {
    public int getCapacity() {
        return capacity;
    }

    int capacity;
}

