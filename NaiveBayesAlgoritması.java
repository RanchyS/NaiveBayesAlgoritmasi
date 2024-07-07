import java.util.Scanner;

public class NaiveBayes {

    public static double olasilikHesapla(String[][] veriSeti, String ozellik) {
        int sayac = 0;
        if (ozellik == "Gunesli" || ozellik == "Bulutlu" || ozellik == "Yagmurlu") { // Hava Durumu mu?
            for (int i = 0; i < veriSeti.length; i++) {
                if (veriSeti[i][0] == ozellik) { // O halde ilk sütunda aramalıyız!
                    sayac++;
                }
            }
        }
        if (ozellik == "Sicak" || ozellik == "Ilik" || ozellik == "Soguk") { // Sıcaklık mı? 
            for (int i = 0; i < veriSeti.length; i++) {
                if (veriSeti[i][1] == ozellik) { // O halde ikinci sütunda aramalıyız!
                    sayac++;
                }
            }
        }
        if (ozellik == "Sert" || ozellik == "Orta" || ozellik == "Hafif") { // Rüzgar mı?
            for (int i = 0; i < veriSeti.length; i++) {
                if (veriSeti[i][2] == ozellik) { // O halde üçüncü sütunda aramalıyız!
                    sayac++;
                }
            }
        }

        if (ozellik == "Evet" || ozellik == "Hayir") { // Piknik bilgisi(etiket) mi?
            for (int i = 0; i < veriSeti.length; i++) {
                if (veriSeti[i][3] == ozellik) { // O halde dördüncü sütunda aramalıyız!
                    sayac++;
                }
            }
        }

        return (double) sayac / veriSeti.length; // eşleşme sayısını sütun sayısına bölmek, olasılığı verir.
    }

    public static double kosulluOlasilikHesapla(String[][] veriSeti, String ozellik1, String ozellik2) {

        int[] indisler = new int[veriSeti.length]; //Koşulu gerektiren kümenin elemanlarının bulunması.
        int indislerElemanSayisi = 0;
        for (int i = 0; i < veriSeti.length; i++) {
            if (ozellik2 == veriSeti[i][3]) { // İki etiketimiz var, "Evet" ve "Hayır" burada bunları buluyoruz.
                indisler[i] = 1; // Eşleşme varsa 1 yoksa 0 olur, dizinin elemanları.
                indislerElemanSayisi++;
            }
        }
        // Özet: Farz edelim ki piknik yapılma durumunu hesaplıyoruz. Koşullu olasılık gereği, 
        // "Evet" etiketine sahip olan satırlarla(yani verilerle) ilgilenmemiz gerekir. 

        int sayac = 0;

        if (ozellik1 == "Gunesli" || ozellik1 == "Bulutlu" || ozellik1 == "Yagmurlu") { // Hava Durumu mu?
            for (int i = 0; i < veriSeti.length; i++) {
                if (indisler[i] == 1) { // Etiket bilgisi ile(Evet ya da Hayır) koşullu olasılığı kontrol ediyoruz!
                    if (veriSeti[i][0] == ozellik1) {
                        sayac++;
                    }
                }
            }
        }
        if (ozellik1 == "Sicak" || ozellik1 == "Ilik" || ozellik1 == "Soguk") { // Sicaklik özelliğine uyarlama
            for (int i = 0; i < veriSeti.length; i++) {
                if (indisler[i] == 1) {
                    if (veriSeti[i][1] == ozellik1) {
                        sayac++;
                    }
                }
            }
        }
        if (ozellik1 == "Sert" || ozellik1 == "Orta" || ozellik1 == "Hafif") { // Rüzgar özelliğine uyarlama
            for (int i = 0; i < veriSeti.length; i++) {
                if (indisler[i] == 1) {
                    if (veriSeti[i][2] == ozellik1) {
                        sayac++;
                    }
                }
            }
        }
        return (double) sayac / indislerElemanSayisi; // Koşullu olasılığın sonucunu döndürür.
    }

    public static String kararVer(String[][] veriSeti, String havaDurumu, String sicaklik, String ruzgar) {
        double piknikYapilmaOlasiligi = (kosulluOlasilikHesapla(veriSeti, havaDurumu, "Evet")
                * kosulluOlasilikHesapla(veriSeti, sicaklik, "Evet")
                * kosulluOlasilikHesapla(veriSeti, ruzgar, "Evet")
                * olasilikHesapla(veriSeti, "Evet"))
                / (olasilikHesapla(veriSeti, havaDurumu)
                * olasilikHesapla(veriSeti, sicaklik)
                * olasilikHesapla(veriSeti, ruzgar));

        double piknikYapilmamaOlasiligi = (kosulluOlasilikHesapla(veriSeti, havaDurumu, "Hayir")
                * kosulluOlasilikHesapla(veriSeti, sicaklik, "Hayir")
                * kosulluOlasilikHesapla(veriSeti, ruzgar, "Hayir")
                * olasilikHesapla(veriSeti, "Hayir"))
                / (olasilikHesapla(veriSeti, havaDurumu)
                * olasilikHesapla(veriSeti, sicaklik)
                * olasilikHesapla(veriSeti, ruzgar));

        // Buraya kadar olan kısım, "Naive Bayes Teoremi" idi.
        String mesaj = "Piknik Yapilma Olasiligi: " + piknikYapilmaOlasiligi + "\n"
                + "Piknik Yapilmama Olasiligi: " + piknikYapilmamaOlasiligi + "\n";
        if (piknikYapilmaOlasiligi >= piknikYapilmamaOlasiligi) {
            mesaj = mesaj + "Sonuc: piknik yapilir!";
        } else {
            mesaj = mesaj + "Sonuc: piknik yapilmaz!";
        }

        // Bir eşik değer belirlemek yerine(mesela %50), yapılma ve yapılmama olasılıklarından büyük
        // olanı referans almayı tercih ettim.
        return mesaj;
    }

    public static void main(String[] args) {
        String[][] veriSeti = { // Verilerin bulunduğu tabloyu matrise dökme işlemi.
            {"Gunesli", "Sicak", "Sert", "Evet"},
            {"Bulutlu", "Ilik", "Hafif", "Evet"},
            {"Gunesli", "Soguk", "Hafif", "Evet"},
            {"Yagmurlu", "Sicak", "Hafif", "Hayir"},
            {"Gunesli", "Soguk", "Sert", "Hayir"},
            {"Bulutlu", "Sicak", "Hafif", "Evet"},
            {"Bulutlu", "Ilik", "Orta", "Evet"},
            {"Yagmurlu", "Ilik", "Sert", "Hayir"},
            {"Bulutlu", "Soguk", "Hafif", "Hayir"},
            {"Yagmurlu", "Sicak", "Orta", "Hayir"}
        };

        // Kullanıcı kısmı
        System.out.println("<Veri seti>\n\n"
                + "Hava Durumu" + "|" + "Sicaklik" + "|" + "Ruzgar" + "|" + "Piknik yapildi mi?\n"
                + "----------------------------------------------");
        for (String veri[] : veriSeti) {
            System.out.println(veri[0] + "\t|" + veri[1] + "\t|" + veri[2] + "\t|" + veri[3]);
        }

        System.out.println("----------------------------------------------");

        //verinin kullanıcıdan alınması istenirse burası kullanılmalı
        String havaDurumu;
        String sicaklik;
        String ruzgar;
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hava Durumu(Bulutlu, Gunesli, Yagmurlu) bilgisini giriniz:");
        havaDurumu = (scanner.nextLine()).intern();
        System.out.println("Sicaklik(Sicak, Ilik, Soguk) bilgisini giriniz:");
        sicaklik = (scanner.nextLine()).intern();
        System.out.println("Ruzgar(Sert, Orta, Hafif) bilgisini giriniz:");
        ruzgar = (scanner.nextLine()).intern();       
                
        System.out.println(kararVer(veriSeti, havaDurumu, sicaklik,ruzgar));
         
        //System.out.println(kararVer(veriSeti, "Bulutlu", "Sicak", "Sert"));

    }
}