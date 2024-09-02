package com.gmail.deniska1406sme;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main( String[] args ) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add apartment");
                    System.out.println("2: add random apartment");
                    System.out.println("3: delete apartment");
                    System.out.println("4: find apartment");
                    System.out.println("5: view apartment");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addApartment(sc);
                            break;
                        case "2":
                            insertRandomApartment(sc);
                            break;
                        case "3":
                            deleteApartment(sc);
                            break;
                        case "4":
                            findApartment(sc);
                            break;
                        case "5":
                            viewApartment();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }

    private static void addApartment(Scanner sc) {
        System.out.print("Enter apartment district: ");
        String district = sc.nextLine();
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();
        System.out.print("Enter apartment area: ");
        Double area = Double.valueOf(sc.nextLine());
        System.out.print("Enter apartment rooms: ");
        Integer rooms = Integer.parseInt(sc.nextLine());
        System.out.print("Enter apartment price: ");
        Double price = Double.valueOf(sc.nextLine());

        em.getTransaction().begin();
        try {
            Apartment apartment = new Apartment(district, address, area, rooms, price);
            em.persist(apartment);
            em.getTransaction().commit();

        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    private static void deleteApartment(Scanner sc) {
        System.out.print("Enter apartment ID: ");
        Long id = Long.valueOf(sc.nextLine());

        Apartment apartment = em.getReference(Apartment.class, id);
        if (apartment == null) {
            System.out.println("Apartment not found");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(apartment);
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    private static void findApartment(Scanner sc) {
        System.out.println("Enter parameter of apartment or leave blank for any:");
        System.out.print("Enter apartment district: ");
        String districtInput = sc.nextLine().trim();
        String district = districtInput.isEmpty() ? null : districtInput;

        System.out.print("Enter apartment minimum area: ");
        String minAreaInput = sc.nextLine().trim();
        Double minArea = minAreaInput.isEmpty() ? null : Double.valueOf(minAreaInput);

        System.out.print("Enter apartment maximum area: ");
        String maxAreaInput = sc.nextLine().trim();
        Double maxArea = maxAreaInput.isEmpty() ? null : Double.valueOf(maxAreaInput);

        System.out.print("Enter apartment rooms: ");
        String roomsInput = sc.nextLine().trim();
        Integer rooms = roomsInput.isEmpty() ? null : Integer.valueOf(roomsInput);

        System.out.print("Enter apartment minimum price: ");
        String minPriceInput = sc.nextLine().trim();
        Double minPrice = minPriceInput.isEmpty() ? null : Double.valueOf(minPriceInput);

        System.out.print("Enter apartment maximum price: ");
        String maxPriceInput = sc.nextLine().trim();
        Double maxPrice = maxPriceInput.isEmpty() ? null : Double.valueOf(maxPriceInput);

        List<Apartment> res = getRecomendedApartment(district, minArea, maxArea, rooms, minPrice, maxPrice);
        for (Apartment apartment : res){
            System.out.println(apartment);
        }


    }

    private static List<Apartment> getRecomendedApartment(String district, Double minArea, Double maxArea, Integer rooms, Double minPrice, Double maxPrice) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Apartment> query = cb.createQuery(Apartment.class);
        Root<Apartment> apartment = query.from(Apartment.class);

        List<Predicate> predicates = new ArrayList<>();
        if(district != null) predicates.add(cb.equal(apartment.get("district"), district));
        if (minArea != null) predicates.add(cb.ge(apartment.get("area"), minArea));
        if (maxArea != null) predicates.add(cb.le(apartment.get("area"), maxArea));
        if (rooms != null) predicates.add(cb.equal(apartment.get("rooms"), rooms));
        if(minPrice != null) predicates.add(cb.ge(apartment.get("price"), minPrice));
        if (maxPrice != null) predicates.add(cb.le(apartment.get("price"), maxPrice));

        query.select(apartment).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Apartment> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    private static void viewApartment() {
        Query query = em.createQuery("SELECT a FROM Apartment a",Apartment.class);
        List<Apartment> apartments = query.getResultList();

        for (Apartment apartment : apartments) {
            System.out.println(apartment);
        }
    }

    private static void insertRandomApartment(Scanner sc) {
        System.out.print("Enter apartment count: ");
        Integer count = Integer.valueOf(sc.nextLine());

        em.getTransaction().begin();
        try {
            for (int i = 0; i < count; i++) {
                Apartment apartment = new Apartment(randomDistrict(),randomStreet(),40 + RND.nextDouble() * 110,
                        1 + RND.nextInt(5),10000 + RND.nextDouble() * 100000);
                em.persist(apartment);
            }
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }

    }
    static final String[] DISTRICTS = {"centre","west","north","east","south"};
    static final String[] STREETS = {"studentskaya ","ubileynaya ","main st ","centralnaya ","shkonaya "};
    static final Random RND = new Random();

    static String randomDistrict() {
        return DISTRICTS[RND.nextInt(DISTRICTS.length)];
    }
    static String randomStreet(){
       return STREETS[RND.nextInt(STREETS.length)] + RND.nextInt(100);
    }


}
