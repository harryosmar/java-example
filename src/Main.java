import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

class Produk {
    private String nama;
    private double harga;
    private String  kategori;
    NumberFormat numberFormat;

    public Produk(String nama, double harga, String kategori,  NumberFormat numberFormat) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.numberFormat = numberFormat;
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
        System.out.println("Isi jumlah pesanan untuk " + this.kategori + " " + nama +" Rp " + numberFormat.format(harga) + ": ");
    }
}

class PesananProduk {
    private Produk produk;
    private int jumlah;
    NumberFormat numberFormat;

    public PesananProduk(Produk produk, int jumlah, NumberFormat numberFormat) {
        this.produk = produk;
        this.jumlah = jumlah;
        this.numberFormat = numberFormat;
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
        return this.jumlah * this.produk.getHarga();
    }

    public void tampilPesananProduk() {
        if (this.jumlah > 0) {
            System.out.println(produk.getCategory() + " " + produk.getNama() + " Rp " + numberFormat.format(produk.getHarga()) + " x " + this.jumlah + " = " + numberFormat.format(this.getTotalHarga()));
        }
    }
}

class Keranjang {
    public PesananProduk nasiGoreng;
    public PesananProduk bakmiGoreng;
    public PesananProduk airMineral;
    public PesananProduk airMineralGratis;
    public PesananProduk tehManis;
    public PesananProduk tehManisGratis;

    private NumberFormat numberFormat;

    public Keranjang(NumberFormat numberFormat) {
        this.nasiGoreng = new PesananProduk(new Produk("nasi goreng", 35000, "makanan", numberFormat), 0, numberFormat);
        this.bakmiGoreng = new PesananProduk(new Produk("bakmi goreng", 45000, "makanan", numberFormat), 0, numberFormat);
        this.airMineral = new PesananProduk(new Produk("air mineral", 3000, "minuman", numberFormat), 0, numberFormat);
        this.airMineralGratis = new PesananProduk(new Produk("air mineral", 0, "minuman", numberFormat), 0, numberFormat);
        this.tehManis = new PesananProduk(new Produk("teh manis", 5000, "minuman", numberFormat), 0, numberFormat);
        this.tehManisGratis = new PesananProduk(new Produk("teh manis", 0, "minuman", numberFormat), 0, numberFormat);

        this.numberFormat = numberFormat;
    }

    public double getTotalHarga() {
        return nasiGoreng.getTotalHarga() + bakmiGoreng.getTotalHarga() + airMineral.getTotalHarga() + tehManis.getTotalHarga() + airMineralGratis.getTotalHarga() + tehManisGratis.getTotalHarga();
    }

    public void tampilPesanan() {
        System.out.println("Total Rp " + numberFormat.format(this.getTotalHarga()));
    }
}

class AplikasiRestoran {
    static double persentaseDiskon = 10; // 10%
    static double persentasePajak = 10; // 10%

    static double serviceFee = 20000;

    static Keranjang keranjang;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); // Set the locale to Indonesia

        keranjang = new Keranjang(numberFormat);

        System.out.println("=========================");
        System.out.println("Masukkan pesanan Anda");
        System.out.println("=========================");

        keranjang.nasiGoreng.getProduk().tampilProduk();
        int jumlahNasiGoreng = scanner.nextInt();
        keranjang.nasiGoreng.setJumlah(jumlahNasiGoreng);

        keranjang.bakmiGoreng.getProduk().tampilProduk();
        int jumlahBakmiGoreng = scanner.nextInt();
        keranjang.bakmiGoreng.setJumlah(jumlahBakmiGoreng);

        keranjang.airMineral.getProduk().tampilProduk();
        int jumlahAirMineral = scanner.nextInt();
        keranjang.airMineral.setJumlah(jumlahAirMineral);

        keranjang.tehManis.getProduk().tampilProduk();
        int jumlahTehManis = scanner.nextInt();
        keranjang.tehManis.setJumlah(jumlahTehManis);


        double totalHarga = keranjang.getTotalHarga();
        boolean dapatDiskon = false;
        if (totalHarga >= 100000) {
            dapatDiskon = true;
        } else if (totalHarga >= 50000) {
            System.out.println("Penawaran untuk Anda, Beli 1 Gratis 1 :");
            System.out.println("0. Tidak menggunakan penawaran");
            System.out.println("1. Air Mineral Rp" + numberFormat.format(keranjang.airMineral.getProduk().getHarga()));
            System.out.println("2. Teh Manis Rp" + numberFormat.format(keranjang.tehManis.getProduk().getHarga()));
            int pilihanGratis = scanner.nextInt();
            if (pilihanGratis == 1) {
                keranjang.airMineral.setJumlah(keranjang.airMineral.getJumlah() + 1);
                keranjang.airMineralGratis.setJumlah(1);
            } else if (pilihanGratis == 2) {
                keranjang.tehManis.setJumlah(keranjang.tehManis.getJumlah() + 1);
                keranjang.tehManisGratis.setJumlah(1);
            }
        }

        System.out.println("============================");
        System.out.println("Billing");
        System.out.println("============================");
        keranjang.nasiGoreng.tampilPesananProduk();
        keranjang.bakmiGoreng.tampilPesananProduk();
        keranjang.airMineral.tampilPesananProduk();
        keranjang.airMineralGratis.tampilPesananProduk();
        keranjang.tehManis.tampilPesananProduk();
        keranjang.tehManisGratis.tampilPesananProduk();
        System.out.println("-----------------------------");

        totalHarga = keranjang.getTotalHarga();
        System.out.println("Total : Rp" + totalHarga);

        if (dapatDiskon) {
            double diskon = totalHarga * persentaseDiskon / 100;
            totalHarga -= diskon;
            System.out.println("Diskon "+ persentaseDiskon  +" % : " + numberFormat.format(diskon));
            System.out.println("Total setelah diskon : Rp" + totalHarga);
        }

        double pajak = (persentasePajak / 100 * totalHarga);
        totalHarga += pajak;
        System.out.println("Pajak: " + persentasePajak + " % : " +numberFormat.format(pajak));

        totalHarga += serviceFee;
        System.out.println("Biaya Layanan: " + numberFormat.format(serviceFee)); // Print service fee in IDR

        System.out.println("Total Biaya Keseluruhan: " + numberFormat.format(totalHarga)); // Print total price in IDR

        System.out.println("============================");
    }
}
