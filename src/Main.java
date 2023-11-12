import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

class Produk {
    private String nama;
    private double harga;
    private String kategori;
    private NumberFormat numberFormat;

    private boolean isInFreeOffer;

    public Produk(String nama, double harga, String kategori, NumberFormat numberFormat, boolean isInFreeOffer) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.numberFormat = numberFormat;
        this.isInFreeOffer = isInFreeOffer;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public void ubahHarga(double harga) {
        this.harga = harga;
    }

    public String getCategory() {
        return kategori;
    }

    public void tampilProduk() {
        System.out.println(this.kategori + " " + nama + " Rp " + numberFormat.format(harga));
    }

    public boolean isInFreeOffer() {
        return isInFreeOffer;
    }
}

class Pesanan {
    private Produk produk;
    private int jumlah;
    NumberFormat numberFormat;

    private double harga;

    public Pesanan(Produk produk, int jumlah, double harga, NumberFormat numberFormat) {
        this.produk = produk;
        this.jumlah = jumlah;
        this.numberFormat = numberFormat;
        this.harga = harga;
    }

    public Produk getProduk() {
        return produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getTotalHarga() {
        return this.jumlah * this.harga;
    }

    public void tampilPesananProduk() {
        if (this.jumlah > 0) {
            System.out.println(produk.getCategory() + " " + produk.getNama() + " Rp " + numberFormat.format(this.harga) + " x " + this.jumlah + " = " + numberFormat.format(this.getTotalHarga()));
        }
    }
}

class Keranjang {
    private NumberFormat numberFormat;

    private Pesanan[] pesanan;

    public Keranjang(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
        this.pesanan = new Pesanan[0];
    }

    public Pesanan[] addToCart(Produk produk, int jumlah, double harga) {
        Pesanan[] newPesanan = new Pesanan[pesanan.length + 1];
        System.arraycopy(pesanan, 0, newPesanan, 0, pesanan.length);
        newPesanan[pesanan.length] = new Pesanan(produk, jumlah, harga, numberFormat);
        pesanan = newPesanan;

        return pesanan;
    }

    public double getTotalHarga() {
        int total = 0;
        for (int i = 0; i < pesanan.length; i++) {
            total += pesanan[i].getTotalHarga();
        }
        return total;
    }

    public void tampilPesanan() {
        System.out.println("Total Rp " + numberFormat.format(this.getTotalHarga()));
    }

    public void tampilBilling(boolean dapatDiskon, double persentaseDiskon, double persentasePajak, double serviceFee) {
        System.out.println("============================");
        System.out.println("Billing");
        System.out.println("============================");
        for (int i = 0; i < pesanan.length; i++) {
            pesanan[i].tampilPesananProduk();
        }
        System.out.println("-----------------------------");


        double totalHarga = this.getTotalHarga();
        System.out.println("Total : Rp" + totalHarga);

        if (dapatDiskon) {
            double diskon = totalHarga * persentaseDiskon / 100;
            totalHarga -= diskon;
            System.out.println("Diskon " + persentaseDiskon + " % : " + numberFormat.format(diskon));
            System.out.println("Total setelah diskon : Rp" + totalHarga);
        }

        double pajak = (persentasePajak / 100 * totalHarga);
        totalHarga += pajak;
        System.out.println("Pajak: " + persentasePajak + " % : " + numberFormat.format(pajak));

        totalHarga += serviceFee;
        System.out.println("Biaya Layanan: " + numberFormat.format(serviceFee)); // Print service fee in IDR

        System.out.println("Total Biaya Keseluruhan: " + numberFormat.format(totalHarga)); // Print total price in IDR
    }
}

class Katalog {
    private Produk[] listProduk;

    private NumberFormat numberFormat;

    public Katalog(Produk[] listProduk, NumberFormat numberFormat) {
        this.listProduk = listProduk;
        this.numberFormat = numberFormat;
    }

    private Produk[] groupPerKategori(String kategori) {
        Produk[] ordered = new Produk[0];
        for (int i = 0; i < listProduk.length; i++) {
            if (listProduk[i].getCategory() == kategori) {
                Produk[] newOrdered = new Produk[ordered.length + 1];
                System.arraycopy(ordered, 0, newOrdered, 0, ordered.length);
                newOrdered[ordered.length] = listProduk[i];
                ordered = newOrdered;
            }
        }

        return ordered;
    }

    public void orderPerKategori() {
        Produk[] makanan = groupPerKategori("makanan");
        Produk[] minuman = groupPerKategori("minuman");

        // Create a new array to store the merged elements
        Produk[] newlistProduk = new Produk[makanan.length + minuman.length];
        System.arraycopy(makanan, 0, newlistProduk, 0, makanan.length);
        System.arraycopy(minuman, 0, newlistProduk, makanan.length, minuman.length);
        listProduk = newlistProduk;
    }

    public Produk[] penawaranGratis() {
        Produk[] freeOffer = new Produk[0];
        for (int i = 0; i < listProduk.length; i++) {
            if (listProduk[i].isInFreeOffer()) {
                Produk[] newFreeOffer = new Produk[freeOffer.length + 1];
                System.arraycopy(freeOffer, 0, newFreeOffer, 0, freeOffer.length);
                newFreeOffer[freeOffer.length] = listProduk[i];
                freeOffer = newFreeOffer;
            }
        }

        return freeOffer;
    }

    public Produk[] getListProduk() {
        return this.listProduk;
    }

    public Produk[] hapusProduk(int idProduk) {
        int indexToRemove = idProduk - 1;
        if (indexToRemove >= 0 && indexToRemove < listProduk.length) {
            // Create a new array with size one less than the original array
            Produk[] newListProduk = new Produk[listProduk.length - 1];

            // Copy elements before the index
            System.arraycopy(listProduk, 0, newListProduk, 0, indexToRemove);

            // Copy elements after the index
            System.arraycopy(listProduk, indexToRemove + 1, newListProduk, indexToRemove, listProduk.length - indexToRemove - 1);

            // Update persediaan to point to the new array
            listProduk = newListProduk;
        }

        return listProduk;
    }

    public Produk[] ubahHarga(int idProduk, double harga) {
        int indexToUpdate = idProduk - 1;
        if (indexToUpdate >= 0 && indexToUpdate < listProduk.length) {
            for (int i = 0; i < listProduk.length; i++) {
                if (i == indexToUpdate) {
                    listProduk[i].ubahHarga(harga);
                }
            }
        }
        return listProduk;
    }

    public Produk[] tambahProduk(String nama, double harga, String kodeKategori) {
        if (kodeKategori.equals("MA")  || kodeKategori.equals("MI")) {
            String kategori = "makanan";
            if (kodeKategori == "MI") {
                kategori = "minuman";
            }

            Produk[] newListProduk = new Produk[listProduk.length + 1];
            System.arraycopy(listProduk, 0, newListProduk, 0, listProduk.length);
            newListProduk[listProduk.length] = new Produk(nama, harga, kategori, this.numberFormat, false);
            listProduk = newListProduk;
        }
        return listProduk;
    }
}

class Pemesanan {
    private Keranjang keranjang;

    private NumberFormat numberFormat;

    private Scanner scanner;

    double persentaseDiskon = 10; // 10%
    double persentasePajak = 10; // 10%
    double serviceFee = 20000;

    public Pemesanan(NumberFormat numberFormat, Scanner scanner) {
        this.numberFormat = numberFormat;
        this.scanner = scanner;
        this.keranjang = new Keranjang(numberFormat);
    }

    public void MenuPemesanan(Katalog katalog) {
        Keranjang keranjang = new Keranjang(numberFormat);

        while (true) {
            System.out.println("\n\n");
            System.out.println("=========================");
            System.out.println("Masukkan pesanan Anda");
            System.out.println("=========================");

            katalog.orderPerKategori();
            Produk[] listProduk = katalog.getListProduk();
            System.out.println("0. untuk berhenti pesan");
            for (int i = 0; i < listProduk.length; i++) {
                System.out.print(i+1 + ". ");
                listProduk[i].tampilProduk();
            }

            System.out.print("Masukkan pilihan 0-"+ listProduk.length +" : ");
            int pilihan = scanner.nextInt();
            if (pilihan > listProduk.length) {
                break;
            }
            if (pilihan == 0) {
                double totalHarga = keranjang.getTotalHarga();
                if (totalHarga >= 50000 && totalHarga < 100000) {
                    System.out.println("Penawaran untuk Anda, Beli 1 Gratis 1 :");
                    System.out.println("0. Tidak menggunakan penawaran");
                    Produk[] freeOffer = katalog.penawaranGratis();
                    for (int i = 0; i < freeOffer.length; i++) {
                        if (freeOffer[i].isInFreeOffer()) {
                            System.out.print(i+1 + ". ");
                            freeOffer[i].tampilProduk();
                        }
                    }
                    System.out.print("Masukkan pilihan 1-"+ freeOffer.length +" : ");
                    int pilihanGratis = scanner.nextInt();
                    if (pilihanGratis > freeOffer.length) {
                        break;
                    }
                    Produk produkGratis = freeOffer[pilihanGratis - 1];
                    keranjang.addToCart(produkGratis, 1, 0);
                }
                break;
            }
            System.out.print("Masukkan jumlah untuk pesanan: ");
            int jumlahProduk = scanner.nextInt();
            Produk produkPilihan = listProduk[pilihan - 1];
            keranjang.addToCart(produkPilihan, jumlahProduk, produkPilihan.getHarga());
        }


        double totalHarga = keranjang.getTotalHarga();
        boolean dapatDiskon = totalHarga >= 100000;
        keranjang.tampilBilling(dapatDiskon, persentaseDiskon, persentasePajak, serviceFee);
    }
}

class AplikasiRestoran {

    static Katalog katalog;

    static NumberFormat numberFormat;

    static Scanner scanner;


    private static void fiturAdmin() {
        while (true) {
            System.out.println("\n\n");
            System.out.println("=====================================");
            System.out.println("Katalog Produk");
            System.out.println("=====================================");
            katalog.orderPerKategori();
            Produk[] listProduk = katalog.getListProduk();
            for (int i = 0; i < listProduk.length; i++) {
                int number = i + 1;
                System.out.print("id produk \""+ number + "\" : ");
                listProduk[i].tampilProduk();
            }
            System.out.println("=====================================");
            System.out.println("0. berhenti dan kembali ke beranda");
            System.out.println("1. Tambah item");
            System.out.println("2. Hapus Item");
            System.out.println("3. Ubah Harga");
            System.out.print("Masukkan pilihan 0-3 : ");
            int pilihan = scanner.nextInt();
            if (pilihan == 1) {
                Scanner newscanner = new Scanner(System.in);
                System.out.print("Masukkan nama : ");
                String nama = newscanner.nextLine().trim();

                System.out.print("Masukkan kategori, MA(Makanan) atau MI(Minuman) : ");
                String kategori = newscanner.nextLine();

                System.out.print("Masukkan harga : ");
                double harga = newscanner.nextDouble();

                katalog.tambahProduk(nama, harga, kategori);
            } else if (pilihan == 2) {
                System.out.print("Masukkan id produk : ");
                int idProduk = scanner.nextInt();
                Scanner newscanner = new Scanner(System.in);
                System.out.print("Apakah Anda yakin menghapus produk id \""+ idProduk +"\" y/n ? ");
                String konfirmasi = newscanner.nextLine().trim();
                if (konfirmasi.equals("y")) {
                    katalog.hapusProduk(idProduk);
                }
            } else if (pilihan == 3) {
                System.out.print("Masukkan id produk : ");
                int idProduk = scanner.nextInt();

                System.out.print("Masukkan harga : ");
                double harga = scanner.nextDouble();

                katalog.ubahHarga(idProduk, harga);
            } else {
                return;
            }
        }
    }

    public static void main(String[] args) {
        numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); // Set the locale to Indonesia
        scanner = new Scanner(System.in);
        Produk[] listProduk = new Produk[]{
                new Produk( "air mineral", 3000, "minuman", numberFormat, true),
                new Produk( "nasi goreng", 35000, "makanan", numberFormat, false),
                new Produk( "teh manis", 5000, "minuman", numberFormat, true),
                new Produk( "bakmie goreng", 45000, "makanan", numberFormat, false),
                new Produk( "bihun goreng", 45000, "makanan", numberFormat, false),
                new Produk( "kweetiau goreng", 45000, "makanan", numberFormat, false),
                new Produk( "es jeruk", 45000, "minuman", numberFormat, false),
                new Produk( "jus", 12000, "minuman", numberFormat, false),
        };
        katalog = new Katalog(listProduk, numberFormat);


        while (true) {
            System.out.println("\n=========================");
            System.out.println("Beranda");
            System.out.println("=========================");
            System.out.println("0. Selesai");
            System.out.println("1. Pengelolaan Menu");
            System.out.println("2. Pemesanan");
            System.out.print("Masukkan Pilihan 0-2: ");
            int pilihanFitur = scanner.nextInt();
            if (pilihanFitur == 1) {
                fiturAdmin();
            } else if (pilihanFitur == 2) {
                Pemesanan pemesanan = new Pemesanan(numberFormat, scanner);
                pemesanan.MenuPemesanan(katalog);
            } else {
                System.out.print("Anda Keluar dari sistem...");
                return;
            }
        }
    }
}
