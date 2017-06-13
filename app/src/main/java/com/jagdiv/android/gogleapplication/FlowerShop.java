package com.jagdiv.android.gogleapplication;

import android.support.annotation.NonNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by JAG on 6/1/2017.
 */

public class FlowerShop {
    static Map<FlowerName, ArrayList<FlowerBundle>> selectedBunch = new HashMap<FlowerName, ArrayList<FlowerBundle>>();

    public static void main(String[] args) {
        //System.out.println("Hi from Rupam Flower Shop");

        new FlowerShop().new DataStructure().createShop(selectedBunch);
        ExecutorService executor = Executors.newSingleThreadExecutor();
      //  for (int i = 0; i <100; i++)
            executor.execute(new FlowerShop().new Order("R12", 15));
       executor.execute(new FlowerShop().new Order("L09", 10));
        executor.execute(new FlowerShop().new Order("T58", 10));//ans 0 not[3 5 9]
        executor.submit(new FlowerShop().new Order("R12", 10));
        executor.submit(new FlowerShop().new Order("L09", 15));
        executor.submit(new FlowerShop().new Order("T58", 13));

        executor.execute(new FlowerShop().new Order("R12", 13));
        executor.execute(new FlowerShop().new Order("L09", 13));
        executor.execute(new FlowerShop().new Order("T58", 15));

        executor.shutdown();
    }

    private class Order implements Runnable {
        String code;
        int qty;

        public Order(String code, int qty) {
            this.code = code;
            this.qty = qty;
            //System.out.println("code " + code + " qty " + qty);
        }

        void process() {

            Map<Integer, Integer> result = new FlowerShop().new DataStructure().pickBundle(this.code, this.qty);
            printOrder(result);
        }

        void printOrder(Map<Integer, Integer> treeMap) {
            Map<Integer, Integer> result = new TreeMap<Integer, Integer>(
                    new Comparator<Integer>() {

                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o2.compareTo(o1);
                        }

                    });
            result.putAll(treeMap);
            Set<Integer> bunch = result.keySet();
            List<FlowerBundle> listFlowerBundle = new DataStructure().listFlowerBundle(code);
            ArrayList<Integer> listNumberBundle = new DataStructure().getListNumberBundle(listFlowerBundle);
            System.out.println(listNumberBundle);

            String totalPrice = new DataStructure().getTotalPrice(new DataStructure().getBunchNumberPrice(listFlowerBundle), result);
            System.out.println(qty + " " + code + " $" + totalPrice);

            for (int bunchnum : bunch) {
                // System.out.println("bunchnum " + bunchnum + " times " + result.get(bunchnum));
                if (bunchnum == 0 && result.get(bunchnum) == 0) {
                    System.out.println(qty + " Qty Minimum " + Collections.min(listNumberBundle) + " bunch is needed. Sorry not able to process ");
                }
                if (bunchnum == -1 && result.get(bunchnum) == -1) {
                    System.out.println("Flower code not valid");
                }
                if (bunchnum > 0 && result.get(bunchnum) > 0) {//bunch and times


                    System.out.println("        " + result.get(bunchnum) + " * " + bunchnum + "  $ " + new DataStructure().searchPriceOfBundle(new DataStructure().getBunchNumberPrice(listFlowerBundle), bunchnum));
                }
                if (bunchnum == -1 && result.get(bunchnum) == 0) {//bunch and times in 5&10 bunch entry of 13(for prime)

                    //System.out.println("*****Code " + code + " listNumberBundle " + listNumberBundle + " Qty " + qty + " bunchnum * times " + bunchnum + "*" + result.get(bunchnum));
                    System.out.println(qty + " Qty " + "*****Code " + code + " listNumberBundle " + listNumberBundle + " sorry not able to procees this bunch ");

                }
                if (bunchnum == 0 && result.get(bunchnum) == -1) {//bunch and times in 3,6,9 bunch entry of 10
                    System.out.println(qty + " Qty " + "*****Code " + code + " listNumberBundle " + listNumberBundle + " no combination found");
                }
            }
        }

        @Override
        public void run() {
            //   System.out.println("in run before process");
            process();
            // System.out.println("in run after process");
        }

        public void start() {

            //System.out.println("in start");
        }
    }

    private class DataStructure {

        public DataStructure() {
        }


        Map<Integer, Integer> pickBundle(String code, int qty) {
            Map<Integer, Integer> returnnum = new HashMap<>();
            List<FlowerBundle> listFlowerBundle = new ArrayList<FlowerBundle>();
            if (searchFlowerType(code)) {
                listFlowerBundle = listFlowerBundle(code);
                ArrayList<Integer> listNumberBundle = getListNumberBundle(listFlowerBundle);
                returnnum = selectionLogic(listNumberBundle, qty);
                //printOrder(returnnum,code,qty);
            } else
                returnnum.put(-1, -1);
            return returnnum;
        }

        @NonNull
        private ArrayList<Integer> getListNumberBundle(List<FlowerBundle> listFlowerBundle) {
            ArrayList<Integer> listNumberBundle = new ArrayList<Integer>();
            for (FlowerBundle key : listFlowerBundle) {
                listNumberBundle.add(key.getBunch());
                // System.out.println("key.getBunch() " + key.getBunch());
            }
            return listNumberBundle;
        }

        Map<Integer, Float> getBunchNumberPrice(List<FlowerBundle> listFlowerBundle) {
            Map<Integer, Float> mapBunchNumberPrice = new HashMap<Integer, Float>();
            for (FlowerBundle key : listFlowerBundle) {
                mapBunchNumberPrice.put(key.getBunch(), key.getPrice());
            }
            return mapBunchNumberPrice;
        }

        float searchPriceOfBundle(Map<Integer, Float> mapBunchNumberPrice, int bunch) {
            float price = 0.0f;
            //ArrayList<Integer> listNumberBundle = new ArrayList<Integer>();
            Set<Integer> setBunchNumber = mapBunchNumberPrice.keySet();
            for (int bunchnum : setBunchNumber) {
                // for(int i=0;i<listFlowerBundle.size();i++)
                if (bunch == bunchnum) {
                    // for (FlowerBundle key : listFlowerBundle) {
                    price = mapBunchNumberPrice.get(bunchnum);
                    //System.out.println("bunch "+bunchnum+ " ** "+price);
                    //}
                }
            }
            return price;
        }

        String getTotalPrice(Map<Integer, Float> mapBunchNumberPrice, Map<Integer, Integer> result) {
            float totalPrice = 0.0f;
            Set<Integer> setBunchNumberPrice = mapBunchNumberPrice.keySet();
            Set<Integer> setresultBunchNumber = result.keySet();
            for (int resultBunchnum : setresultBunchNumber) {

                if (resultBunchnum > 0) {
                    totalPrice = totalPrice + result.get(resultBunchnum) * searchPriceOfBundle(mapBunchNumberPrice, resultBunchnum);

                }
                // System.out.println("resultbunch num"+resultBunchnum+"totalp"+totalPrice);
            }
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String totalPriceString = formatter.format(totalPrice);

            return totalPriceString;
        }

        Map<Integer, Integer> selectionLogic(List<Integer> bundle, int qty) {
            Map<Integer, Integer> mapNumber = new HashMap<Integer, Integer>();
/*            System.out.println("Before Sorting:");
            for (int counter : bundle) {
                  System.out.println(counter);
            }
            Collections.sort(bundle,Collections.<Integer>reverseOrder());
            System.out.println("after Sorting:");
            for (int counter : bundle) {
                System.out.println(counter);
            }
            System.out.println("Collections.min(list)" + Collections.min(bundle));
            System.out.println("minimum number" + bundle.get(bundle.size() - 1));
*/
            Collections.sort(bundle, Collections.<Integer>reverseOrder());

            if (qty < Collections.min(bundle)) {
                System.out.println(qty + " not able to proccess for qty" + "minimum needed qty is " + bundle.get(bundle.size() - 1));
                mapNumber.put(0, 0);
                return mapNumber;
            }
            int count = 0;
            int quantity = qty;

            while (qty > 0) {

                mapNumber.clear();

                qty = recursiveFor(bundle, qty, mapNumber, count);
                //  System.out.println("qty in while " + qty + " count " + count);
                if (count == (bundle.size() - 1)) {
                    break;
                }
                count = count + 1;
                //     if(quantity-qty>0)
                //       System.out.println("count"+count+ "qty"+qty);

            }
          //  System.out.println("second "+mapNumber.get(0));

            return mapNumber;
        }

        private int recursiveFor(List<Integer> bundle, int qty, Map<Integer, Integer> mapNumber, int count) {
            // mapNumber.clear();
            //System.out.println("****count " + count);
            for (int counter = count; counter < bundle.size(); counter++) {
                if (qty > 0) {
                    int times;
                    // System.out.println("bundle.get(counter) " + bundle.get(counter) + " qty/bundle.get(counter) " + qty / bundle.get(counter) + " qty/bundle.get(counter)  " + qty % qty / bundle.get(counter));
                    if (qty % bundle.get(counter) == 0) {
                        //if(counter==0 || !mapNumber.isEmpty() ){
                        times = qty / bundle.get(counter);
                        if(mapNumber.get(bundle.get(counter))!=null) {
                            if (mapNumber.get(bundle.get(counter)) >= 1) {
                                mapNumber.put(bundle.get(counter), mapNumber.get(bundle.get(counter)) + times);
                            }
                        }else
                            mapNumber.put(bundle.get(counter), times);

                        qty = qty - times * bundle.get(counter);
                        break;
                        //}
                    } else {
                        if (qty < bundle.get(counter)) {
                            if (qty < Collections.min(bundle)) {//3%5(10,5 qty 13 not possible)
                                mapNumber.clear();
                                mapNumber.put(-1, 0);
                                qty = -1;//just no more outer while loop needed
                                break;
                            }
                        } else {
                            /*if(qty/bundle.get(counter)>0) {//25/9 R12 and L09 works perfectly
                                //System.out.println("qty%bundle.get(counter)"+qty%bundle.get(counter));
                                //System.out.println("qty/bundle.get(counter)"+qty/bundle.get(counter));
                                mapNumber.put(bundle.get(counter), qty/bundle.get(counter));
                                qty= qty - (bundle.get(counter) *(qty / bundle.get(counter)));
                                //System.out.println("**qty***"+qty);
                                continue;
                            }*/
                            if (bundle.contains(qty - bundle.get(counter))) {//working qty 15  for 9,6,3 ans 9+6
                                //qty 10 for 10,5 done by recursive
                                times = qty / bundle.get(counter);
                               // if(qty%bundle.get(counter))
                                mapNumber.put(bundle.get(counter), times);
                                qty = qty - bundle.get(counter);
                                continue;
                            }
                            ArrayList<Integer> factors = allFactors(qty);
                            if (Collections.disjoint(factors, bundle)) {//returns true if no commmon //13%9(9,5,3 it do by5*2+3)
                                if (bundle.size() > 2) {
                                    if (!(bundle.contains(qty - bundle.get(counter)))) {//assuming 3 is maximum in  a bundle
                                        if ((counter + 2) < 3) {
                                            qty = qty - bundle.get(counter + 2);
                                            //time calculation is tedious to do
                                            mapNumber.put(bundle.get(counter + 2), 1);

                                            if (qty % bundle.get(counter) == 0) {
                                                //if(counter==0 || !mapNumber.isEmpty() ){
                                                times = qty / bundle.get(counter);

                                                if(mapNumber.get(bundle.get(counter))!=null) {
                                                    if (mapNumber.get(bundle.get(counter)) >= 1) {
                                                        mapNumber.put(bundle.get(counter), mapNumber.get(bundle.get(counter)) + times);
                                                    }
                                                }else
                                                    mapNumber.put(bundle.get(counter), times);

                                                qty = qty - times * bundle.get(counter);

                                                //}
                                            }

                                            continue;
                                        }
                                    }
                                }  //R12 5,10
                                mapNumber.clear();
                                mapNumber.put(0, -1);
                                qty = 0;
                                break;
                                //}
                            }


                            //recursiveFor( bundle, qty, mapNumber, count+1);
                        }
                    }
                } else
                    break;
            }//for
            return qty;
        }


        public ArrayList<Integer> allFactors(int a) {

            int upperlimit = (int) (Math.sqrt(a));
            ArrayList<Integer> factors = new ArrayList<Integer>();
            for (int i = 1; i <= upperlimit; i += 1) {
                if (a % i == 0) {
                    factors.add(i);
                    if (i != a / i) {
                        factors.add(a / i);
                    }
                }
            }
            Collections.sort(factors);
            return factors;
        }


        Set<FlowerName> getListFlowerType() {
            //ArrayList<FlowerName> flowerList=new  ArrayList<FlowerName>();
            Set<FlowerName> flowerTypes = selectedBunch.keySet();
            return flowerTypes;
        }

        List<FlowerBundle> listFlowerBundle(String flowerType) {
            List<FlowerBundle> listFlowerBundle = new ArrayList<FlowerBundle>();
            Set<FlowerName> setflowerTypes = getListFlowerType();
            for (FlowerName key : setflowerTypes) {
                //System.out.println("flowernCode" + key.getCode());
                if (flowerType.equals(key.getCode())) {
                    listFlowerBundle = selectedBunch.get(key);
                    return listFlowerBundle;
                }
            }
            return listFlowerBundle;
        }


        boolean searchFlowerType(String code) {
            boolean returnType = false;
            Set<FlowerName> setflowerTypes = getListFlowerType();
            for (FlowerName key : setflowerTypes) {
                //System.out.println(key.getCode());
                if (code.equals(key.getCode()))
                    return true;
            }

            return returnType;
        }

        private void createShop(Map<FlowerName, ArrayList<FlowerBundle>> selectedBunch) {
            ArrayList<FlowerName> flowerType = new ArrayList<FlowerName>(3);
            flowerType.add((FlowerName) new FlowerShop().new FlowerName("R12", "Roses"));
            flowerType.add((FlowerName) new FlowerShop().new FlowerName("L09", "Lilies"));
            flowerType.add((FlowerName) new FlowerShop().new FlowerName("T58", "Tulips"));

            ArrayList<FlowerBundle> roseBundleType = new ArrayList<FlowerBundle>(2);
            roseBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(5, 6.99f));
            roseBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(10, 12.99f));

            selectedBunch.put(flowerType.get(0), roseBundleType);

            ArrayList<FlowerBundle> lillyBundleType = new ArrayList<FlowerBundle>(3);
            lillyBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(3, 9.95f));
            lillyBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(6, 16.95f));
            lillyBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(9, 24.95f));

            selectedBunch.put(flowerType.get(1), lillyBundleType);

            ArrayList<FlowerBundle> tulipBundleType = new ArrayList<FlowerBundle>(3);
            tulipBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(3, 5.95f));
            tulipBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(5, 9.95f));
            tulipBundleType.add((FlowerBundle) new FlowerShop().new FlowerBundle(9, 16.99f));

            selectedBunch.put(flowerType.get(2), tulipBundleType);
        }

    }


    private class FlowerBundle {
        int bunch;
        float price;

        public FlowerBundle(int bunch, float price) {
            this.bunch = bunch;
            this.price = price;
        }

        public int getBunch() {
            return bunch;
        }

        public void setBunch(int bunch) {
            this.bunch = bunch;
        }

        //  public float getPriceByBunch(int bunch){return price; }
        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }

    private class FlowerName {
        String code;
        String name;

        public FlowerName(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
