import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

class Produk {

    private int id;

    private String nama;
    private double harga;
    private String kategori;
    private NumberFormat numberFormat;

    private boolean isInFreeOffer;

    public Produk(int id, String nama, double harga, String kategori, NumberFormat numberFormat, boolean isInFreeOffer) {
        this.id = id;
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

    public String getCategory() {
        return kategori;
    }

    public void tampilProduk() {
        System.out.println(this.kategori + " " + nama + " Rp " + numberFormat.format(harga));
    }

    public int getId() {
        return id;
    }

    public boolean isInFreeOffer() {
        return isInFreeOffer;
    }
}

class PesananProduk {
    private Produk produk;
    private int jumlah;
    NumberFormat numberFormat;

    private double harga;

    public PesananProduk(Produk produk, int jumlah, double harga, NumberFormat numberFormat) {
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

    private PesananProduk[] pesanan;

    public Keranjang(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
        this.pesanan = new PesananProduk[0];
    }

    public PesananProduk[] addToCart(Produk produk, int jumlah, double harga) {
        PesananProduk[] newPesanan = new PesananProduk[pesanan.length + 1];
        System.arraycopy(pesanan, 0, newPesanan, 0, pesanan.length);
        newPesanan[pesanan.length] = new PesananProduk(produk, jumlah, harga, numberFormat);
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

class AplikasiRestoran {
    static double persentaseDiskon = 10; // 10%
    static double persentasePajak = 10; // 10%

    static double serviceFee = 20000;

    static Produk[] listProduk;

    static NumberFormat numberFormat;

    static Scanner scanner;

    private static void tampilPilihanPesanan(String categori) {
        for (int i = 0; i < listProduk.length; i++) {
            if (listProduk[i].getCategory() == categori) {
                System.out.print(i+1 + ". ");
                listProduk[i].tampilProduk();
            }
        }
    }

    private static Produk[] getFreeOffer() {
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

    private static void fiturPemesanan() {
        Keranjang keranjang = new Keranjang(numberFormat);

        while (true) {
            System.out.println("=========================");
            System.out.println("Masukkan pesanan Anda");
            System.out.println("=========================");
            tampilPilihanPesanan("makanan");
            tampilPilihanPesanan("minuman");
            System.out.println("Ketik 0 untuk berhenti pesan");
            System.out.print("Masukkan pilihan 1-"+ listProduk.length +" : ");
            int pilihan = scanner.nextInt();
            if (pilihan > listProduk.length) {
                break;
            }
            if (pilihan == 0) {
                double totalHarga = keranjang.getTotalHarga();
                if (totalHarga >= 50000) {
                    System.out.println("Penawaran untuk Anda, Beli 1 Gratis 1 :");
                    System.out.println("0. Tidak menggunakan penawaran");
                    Produk[] freeOffer = getFreeOffer();
                    for (int i = 0; i < freeOffer.length; i++) {
                        if (freeOffer[i].isInFreeOffer()) {
                            System.out.print(i+1 + ". ");
                            freeOffer[i].tampilProduk();
                        }
                    }
                    System.out.print("Masukkan pilihan 1-"+ freeOffer.length +" : ");
                    int pilihanGratis = scanner.nextInt();
                    if (pilihanGratis > freeOffer.length) {
                        return;
                    }
                    keranjang.addToCart(freeOffer[pilihanGratis - 1], 1, 0);
                }
                break;
            }
            System.out.print("Masukkan jumlah untuk pesanan: ");
            int jumlahProduk = scanner.nextInt();
            keranjang.addToCart(listProduk[pilihan], jumlahProduk, listProduk[pilihan - 1].getHarga());
        }


        double totalHarga = keranjang.getTotalHarga();
        boolean dapatDiskon = totalHarga >= 100000;
        keranjang.tampilBilling(dapatDiskon, persentaseDiskon, persentasePajak, serviceFee);
    }

    private static void fiturAdmin() {
        System.out.println("=========================");
        System.out.println("Menu Admin");
        System.out.println("=========================");
        System.out.println("1. Tambah item");
        System.out.println("2. Hapus Item");
        System.out.println("3. Ubah Harga");
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); // Set the locale to Indonesia

        listProduk = new Produk[]{
                new Produk(1, "nasi goreng", 35000, "makanan", numberFormat, false),
                new Produk(2, "bakmie goreng", 45000, "makanan", numberFormat, false),
                new Produk(3, "air mineral", 3000, "minuman", numberFormat, true),
                new Produk(4, "teh manis", 5000, "minuman", numberFormat, true),
        };


        while (true) {
            System.out.println("=========================");
            System.out.println("Beranda");
            System.out.println("=========================");
            System.out.println("1. Pengelolaan Menu");
            System.out.println("2. Pemesanan");
            System.out.println("3. Selesai");
            System.out.print("Masukkan Pilihan 1-3: ");
            int pilihanFitur = scanner.nextInt();
            if (pilihanFitur == 1) {
                fiturAdmin();
            } else if (pilihanFitur == 2) {
                fiturPemesanan();
            } else {
                System.out.print("Anda Keluar dari sistem...");
                return;
            }
        }
    }
}
