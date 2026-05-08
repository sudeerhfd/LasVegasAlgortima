/**
 * ============================================================
 *   Las Vegas Algoritmasi - Randomize Algoritmalar Odevi
 * ============================================================
 *  Ogrenci No  : 1240505009
 *  Algoritma   : Las Vegas
 *  Veri Boyutu : n = 10^6
 *  Seed        : 1240505009
 *  Problem     : Rastgele uretilmis dizide mod 7 = 0 olan
 *                elemanlari rassal arama ile bulmak.
 *  Basari      : %100 dogru sonuc - hedefi bulana kadar calis.
 * ============================================================
 */

import java.util.Random;

public class LasVegasAlgorithm {

    // ─── PARAMETRELER ────────────────────────────────────────
    static final long STUDENT_ID  = 1240505009L;
    static final long SEED        = STUDENT_ID;
    static final int  N           = 1_000_000;   // 10^6
    static final int  MOD_VALUE   = 7;
    static final int  MOD_TARGET  = 0;
    static final int  RUNS        = 100;

    // ═══════════════════════════════════════════════════════════
    //  1) VERİ SETİ URETIMI
    // ═══════════════════════════════════════════════════════════
    /**
     * Seed ile deterministik olarak n adet rastgele tam sayi uretir.
     * Aralik: [1, 10*N]  =>  mod 7 = 0 olan elemanlarin orani yaklasik 1/7
     */
    static int[] generateDataset() {
        Random rng = new Random(SEED);
        int[] data = new int[N];
        for (int i = 0; i < N; i++) {
            data[i] = rng.nextInt(10 * N) + 1;   // [1, 10*N]
        }
        return data;
    }

    // ═══════════════════════════════════════════════════════════
    //  2) LAS VEGAS ALGORİTMASI
    // ═══════════════════════════════════════════════════════════
    /**
     * Las Vegas: Hedef bulunana kadar rastgele indeks sec.
     * Garantisi : Her zaman DOGRU sonucu dondurur.
     * Donuş     : {bulunan_deger, bulunan_indeks, adim_sayisi}
     */
    static long[] lasVegasSearch(int[] data, Random rng) {
        long steps = 0;
        while (true) {
            int idx = rng.nextInt(N);
            steps++;
            if (data[idx] % MOD_VALUE == MOD_TARGET) {
                return new long[]{data[idx], idx, steps};
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  3) TEORİK BEKLENEN ADIM HESABI  E[X]
    // ═══════════════════════════════════════════════════════════
    /**
     * p  = (mod 7 = 0 olan eleman sayisi) / N
     * E[X] = 1 / p          (geometrik dagilim beklentisi)
     *
     * Rassal ornekleme ile her cekim bagimsiz oldugu icin
     * basari olasiligi p olan geometrik dagilimdan:
     *   E[X] = 1/p
     */
    static double theoreticalExpectedSteps(int[] data) {
        long count = 0;
        for (int x : data) {
            if (x % MOD_VALUE == MOD_TARGET) count++;
        }
        double p = (double) count / N;
        System.out.printf("%n=== Teorik Analiz ===%n");
        System.out.printf("Mod 7 = 0 olan eleman sayisi : %d%n", count);
        System.out.printf("Basari olasiligi p           : %.6f (teorik ~1/7 = %.6f)%n",
                          p, 1.0 / 7);
        System.out.printf("Teorik E[X] = 1/p            : %.4f adim%n", 1.0 / p);
        return 1.0 / p;
    }

    // ═══════════════════════════════════════════════════════════
    //  4) 100 KERE CALISTIR - DENEYSEL ANALIZ
    // ═══════════════════════════════════════════════════════════
    static void runExperiment(int[] data) {
        long[]   stepResults = new long[RUNS];
        double[] timeResults = new double[RUNS];   // nanosaniye

        // Her run icin farkli ama tekrarlanabilir seed: SEED + run
        for (int run = 0; run < RUNS; run++) {
            Random rng = new Random(SEED + run);
            long startNs = System.nanoTime();
            long[] result = lasVegasSearch(data, rng);
            long endNs   = System.nanoTime();

            stepResults[run] = result[2];
            timeResults[run] = (endNs - startNs) / 1_000_000.0;  // ms
        }

        // ── İstatistikler ──
        double avgSteps = mean(stepResults);
        double stdSteps = stdDev(stepResults, avgSteps);

        double avgTime  = meanD(timeResults);
        double stdTime  = stdDevD(timeResults, avgTime);

        long minSteps   = min(stepResults);
        long maxSteps   = max(stepResults);

        System.out.printf("%n=== Deneysel Sonuclar (100 Run) ===%n");
        System.out.printf("Ortalama adim sayisi    : %.2f%n",   avgSteps);
        System.out.printf("Adim std sapmasi        : %.2f%n",   stdSteps);
        System.out.printf("Min adim                : %d%n",     minSteps);
        System.out.printf("Max adim                : %d%n",     maxSteps);
        System.out.printf("Ortalama sure (ms)      : %.4f ms%n",avgTime);
        System.out.printf("Sure std sapmasi (ms)   : %.4f ms%n",stdTime);
    }

    // ═══════════════════════════════════════════════════════════
    //  5) KARSILASTIRMA: TEORIK vs DENEYSEL
    // ═══════════════════════════════════════════════════════════
    static void compareTheoryVsExperiment(int[] data) {
        // Teorik deger
        long count = 0;
        for (int x : data) { if (x % MOD_VALUE == MOD_TARGET) count++; }
        double p       = (double) count / N;
        double theory  = 1.0 / p;

        // Deneysel ortalama (ayri seed'lerle)
        long totalSteps = 0;
        for (int run = 0; run < RUNS; run++) {
            Random rng = new Random(SEED + run);
            long[] res = lasVegasSearch(data, rng);
            totalSteps += res[2];
        }
        double experimental = (double) totalSteps / RUNS;

        double errorPct = Math.abs(theory - experimental) / theory * 100.0;

        System.out.printf("%n=== Teorik vs Deneysel Karsilastirma ===%n");
        System.out.printf("Teorik    E[X]           : %.4f adim%n", theory);
        System.out.printf("Deneysel ort. adim       : %.4f adim%n", experimental);
        System.out.printf("Sapma orani              : %.2f%%%n",    errorPct);
        System.out.printf("%nYorum: Buyuk orneklem boyutunda (n=10^6, 100 run)%n");
        System.out.printf("deneysel ortalama teorik E[X]'e yakin cikmasi%n");
        System.out.printf("Buyuk Sayilar Kanunu'nu dogrulamaktadir.%n");
    }

    // ═══════════════════════════════════════════════════════════
    //  6) RASTALLIGIN STANDART SAPMAYA ETKİSİ
    // ═══════════════════════════════════════════════════════════
    /**
     * Las Vegas'ta her arama bagimsiz, geometrik dagilan bir rastgele
     * degisken X ~ Geom(p).
     *   Var[X] = (1-p) / p^2
     *   Std[X] = sqrt((1-p)) / p
     *
     * Bu deger buyuk oldugunda bile ORTALAMA teorik deger E[X]=1/p'dir;
     * rastgellik calisma suresinin sapmasini arttirir, dogrulugu degil.
     */
    static void stdDevAnalysis(int[] data) {
        long count = 0;
        for (int x : data) { if (x % MOD_VALUE == MOD_TARGET) count++; }
        double p           = (double) count / N;
        double theoreticalStd = Math.sqrt((1 - p) / (p * p));

        // Deneysel std
        long[] steps = new long[RUNS];
        for (int run = 0; run < RUNS; run++) {
            Random rng = new Random(SEED + run);
            steps[run] = lasVegasSearch(data, rng)[2];
        }
        double avg = mean(steps);
        double expStd = stdDev(steps, avg);

        System.out.printf("%n=== Rastlaligin Standart Sapmaya Etkisi ===%n");
        System.out.printf("Teorik  Std[X] = sqrt((1-p)/p^2) : %.4f%n", theoreticalStd);
        System.out.printf("Deneysel Std[X]                  : %.4f%n", expStd);
        System.out.printf("%nYorum: Std sapmasi yuksek gorunse de ORTALAMA%n");
        System.out.printf("her zaman E[X]=1/p=%.2f civarinda kalir.%n", 1.0/p);
        System.out.printf("Rastgellik sure'yi degistirirken sonucu degistirmez.%n");
    }

    // ═══════════════════════════════════════════════════════════
    //  YARDIMCI - İstatistik Fonksiyonlari
    // ═══════════════════════════════════════════════════════════
    static double mean(long[] a) {
        long s = 0; for (long v : a) s += v; return (double)s / a.length;
    }
    static double meanD(double[] a) {
        double s = 0; for (double v : a) s += v; return s / a.length;
    }
    static double stdDev(long[] a, double avg) {
        double s = 0; for (long v : a) s += (v - avg) * (v - avg);
        return Math.sqrt(s / a.length);
    }
    static double stdDevD(double[] a, double avg) {
        double s = 0; for (double v : a) s += (v - avg) * (v - avg);
        return Math.sqrt(s / a.length);
    }
    static long min(long[] a) { long m = a[0]; for (long v:a) if(v<m)m=v; return m; }
    static long max(long[] a) { long m = a[0]; for (long v:a) if(v>m)m=v; return m; }

    // ═══════════════════════════════════════════════════════════
    //  MAIN
    // ═══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  Las Vegas Algoritmasi - Randomize Algoritmalar Odevi");
        System.out.println("============================================================");
        System.out.printf("  Ogrenci No  : %d%n", STUDENT_ID);
        System.out.printf("  Seed        : %d%n", SEED);
        System.out.printf("  Veri boyutu : n = %,d%n", N);
        System.out.printf("  Kосул       : eleman mod %d == %d%n", MOD_VALUE, MOD_TARGET);
        System.out.println("============================================================");

        // 1) Veri seti olustur
        System.out.print("\nVeri seti uretiliyor...");
        long t0 = System.nanoTime();
        int[] data = generateDataset();
        System.out.printf(" %.2f ms%n", (System.nanoTime()-t0)/1e6);

        // 2) Tek ornek calistirma (demo)
        System.out.println("\n--- Tek Ornek Calistirma ---");
        Random demoRng = new Random(SEED);
        long[] demo = lasVegasSearch(data, demoRng);
        System.out.printf("Bulunan deger : %d%n", demo[0]);
        System.out.printf("Indeks        : %d%n", demo[1]);
        System.out.printf("Adim sayisi   : %d%n", demo[2]);
        System.out.printf("Dogrulama     : %d %% %d = %d (== 0 olmali)%n",
                          (int)demo[0], MOD_VALUE, (int)demo[0] % MOD_VALUE);

        // 3) Teorik E[X]
        theoreticalExpectedSteps(data);

        // 4) 100 run deneysel analiz
        runExperiment(data);

        // 5) Karsilastirma
        compareTheoryVsExperiment(data);

        // 6) Std sapma analizi
        stdDevAnalysis(data);

        System.out.println("\n============================================================");
        System.out.println("  Analiz tamamlandi.");
        System.out.println("============================================================");
    }
}
