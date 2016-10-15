package com.okason.prontoshop.data;

import com.okason.prontoshop.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 4/6/2016.
 */
public class SampleProductData {
    public static List<Product> getSampleProducts(){
        List<Product> products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductName("Zebra Zp 450 ZP450 Thermal Label BarCode Printer USB");
        product1.setCategoryName("Electronics");
        product1.setDescription("Great for printing UPS WorldShip shipping labels with a fixed media width. Compact enough for use at home or in office.");
        product1.setImagePath("http://ecx.images-amazon.com/images/I/817v-KYn4CL._SL1500_.jpg");
        product1.setSalePrice(207.00);
        products.add(product1);

        Product product2 = new Product();
        product2.setProductName("HP CE255X Toner Cartridge (HP 55X)");
        product2.setCategoryName("Electronics");
        product2.setDescription("A&D Products Compatible Toner Cartridge Replacement for HP CE255X is the affordable alternative to OEM product.");
        product2.setImagePath("http://ecx.images-amazon.com/images/I/71JqIY%2BnfxL._SL1500_.jpg");
        product2.setSalePrice(24.99);
        products.add(product2);

        Product product3 = new Product();
        product3.setProductName("Buddy Products Key Safe Platinum (3221-32)");
        product3.setCategoryName("Electronics");
        product3.setDescription("Buddy Products is committed to providing quality products at competitive prices. Keep your valuables protected and secure in your home or office.");
        product3.setImagePath("http://ecx.images-amazon.com/images/I/51GWtJMIxXL.jpg");
        product3.setSalePrice(144.20);
        products.add(product3);

        Product product4 = new Product();
        product4.setProductName("Electronics Station, Cutting Boards; Handcrafted of Black and Clear Cast Acrylic");
        product4.setCategoryName("Electronics");
        product4.setDescription("Multipurpose organizer: office desktop file sorter for mail, letter, folders, envelopes; upscale look");
        product4.setImagePath("http://ecx.images-amazon.com/images/I/51UJLEk2ptL._SL1000_.jpg");
        product4.setSalePrice(17.99);
        products.add(product4);

        Product product5 = new Product();
        product5.setProductName("Desktop File Sorter; Mail, Letter, Folder Collator");
        product5.setCategoryName("Electronics");
        product5.setDescription("High-accuracy readings for hefty weights: Capacity: 110lb/50kg, Graduation: 0.1lb/50g");
        product5.setImagePath("http://ecx.images-amazon.com/images/I/61zVa7XIQpL._SL1500_.jpg");
        product5.setSalePrice(7.99);
        products.add(product5);

        Product product6 = new Product();
        product6.setProductName("Canon PIXMA MX922 Wireless Office All-In-One Printer");
        product6.setCategoryName("Electronics");
        product6.setDescription("Print wirelessly and effortlessly from your compatible iPhone, iPad, or iPod touch- no drivers needed!");
        product6.setImagePath("http://ecx.images-amazon.com/images/I/61NNyBDr1gL._SL1000_.jpg");
        product6.setSalePrice(98.99);
        products.add(product6);

        Product product7 = new Product();
        product7.setProductName("Scotch Thermal Laminator, 2 Roller System, Fast Warm-up");
        product7.setCategoryName("Electronics");
        product7.setDescription("Two temperature settings for laminating 3 mil and 5 mil pouches with professional quality finish");
        product7.setImagePath("http://ecx.images-amazon.com/images/I/81wDnAGE1aL._SL1500_.jpg");
        product7.setSalePrice(98.99);
        products.add(product7);

        Product product8 = new Product();
        product8.setProductName("JARBO 3Color Replacement Ink Cartridge For HP 951 Ink Cartridge");
        product8.setCategoryName("Electronics");
        product8.setDescription("Affordable, With chip and ink(Non-OEM),work with all new printers. Show accurate ink levels.");
        product8.setImagePath("http://ecx.images-amazon.com/images/I/61uYs%2BGoi4L._SL1000_.jpg");
        product8.setSalePrice(18.77);
        products.add(product8);

        Product product9 = new Product();
        product9.setProductName("USB Voice Recorder + 8GB Flash Drive");
        product9.setCategoryName("Electronics");
        product9.setDescription("SIMPLE AND EASY TO USE AND CHARGE Single ON/OFF switch for hassle-free operation as a spy USB recorder.");
        product9.setImagePath("http://ecx.images-amazon.com/images/I/61uYs%2BGoi4L._SL1000_.jpg");
        product9.setSalePrice(24.95);
        products.add(product9);

        Product product10 = new Product();
        product10.setProductName("Scotch Thermal Laminator 2 Roller System (TL901)");
        product10.setCategoryName("Electronics");
        product10.setDescription("Laminates items up to 9 inches wide. The TL901 is designed to be used in a home or small office environment");
        product10.setImagePath("http://ecx.images-amazon.com/images/I/51Nnbb6nMLL._SL1017_.jpg");
        product10.setSalePrice(30.24);
        products.add(product10);

        Product product11 = new Product();
        product11.setProductName("Kantek CS200B Mobile Stand for CPU");
        product11.setCategoryName("Computers");
        product11.setDescription("Slim sleek stand elevates your CPU for easier access, Adjustable to accommodate most CPUs");
        product11.setImagePath("http://ecx.images-amazon.com/images/I/61uYs%2BGoi4L._SL1000_.jpg");
        product11.setSalePrice(30.24);
        products.add(product11);

        Product product12 = new Product();
        product12.setProductName("Amazon Basics Metal Monitor Stand - Black");
        product12.setCategoryName("Computers");
        product12.setDescription("Slim sleek stand elevates your CPU for easier access, Adjustable to accommodate most CPUs");
        product12.setImagePath("http://ecx.images-amazon.com/images/I/71mHiO8mECL._SL1500_.jpg");
        product12.setSalePrice(14.64);
        products.add(product12);










        return products;
    }
}
