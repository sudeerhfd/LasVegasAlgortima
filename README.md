# Las Vegas Algoritması ile Rastgele Arama Analizi

Bu proje, büyük bir veri seti içerisinde belirli bir koşulu sağlayan elemanların bulunmasını amaçlayan **Las Vegas** randomize algoritma yaklaşımının deneysel ve matematiksel analizini içermektedir.

## 📌 Proje Tanımı
Proje kapsamında, $10^6$ elemanlı rastgele üretilmiş bir tam sayı dizisinde, $x \equiv 0 \pmod{7}$ (7 ile tam bölünebilme) koşulunu sağlayan bir elemanın konumu Las Vegas yaklaşımıyla aranmıştır. 

### Parametreler
- **Algoritma Tipi:** Las Vegas (Öğrenci no sonu tek olduğu için)
- **Veri Hacmi (n):** $10^6$ (Öğrenci no son rakamı $\geq 5$ olduğu için)
- **Seed Zorunluluğu:** Rastgele sayı üreticisi, öğrenci numarası (`1240505009`) ile beslenmiştir.
- **Koşul:** `eleman % 7 == 0`

## 🧪 Algoritma Yaklaşımı: Las Vegas
Las Vegas algoritmaları, sonucu her zaman **%100 doğru** döndüren ancak çalışma süresi (adım sayısı) rastgele olan algoritmalardır. Bu projede algoritma, hedef elemanı bulana kadar rastgele indeksler seçmeye devam eder.

## 📐 Matematiksel İspat

### 1. Beklenen Adım Sayısı ($E[X]$)
Arama işlemi, başarı olasılığı $p$ olan bir Geometrik Dağılım serisidir. 
- Dizideki toplam eleman ($n$): $1.000.000$
- Koşulu sağlayan eleman sayısı ($k$): $\approx 142.857$ (İstatistiksel beklenti)
- Başarı olasılığı ($p$): $k / n \approx 0.1428$

**Beklenen Değer Formülü:**
$$E[X] = \frac{1}{p} = \frac{1}{0.1428} \approx 7.0$$

### 2. Standart Sapma ve Varyans
Las Vegas algoritmalarında zaman karmaşıklığındaki değişkenliği ölçmek için Varyans kullanılır:
$$Var(X) = \frac{1 - p}{p^2}$$
Standart sapmanın yüksek olması, rastgeleliğin çalışma süresi üzerindeki dalgalanma etkisini ispatlar.

## 📊 Deneysel Sonuçlar (100 Çalıştırma)
Algoritma 100 kez bağımsız olarak koşturulmuş ve aşağıdaki ortalamalar elde edilmiştir:

| Metrik | Teorik Değer | Deneysel Değer |
| :--- | :---: | :---: |
| Ortalama Adım Sayısı | 7.00 | 7.12 |
| Ortalama Süre (ms) | - | 0.0017 ms |
| Sapma Oranı | %0 | %1.86 |

> **Analiz:** Deneysel sonuçların teorik değerlere %1.86 gibi düşük bir farkla yakınsaması, **Büyük Sayılar Kanunu**'nu ve olasılıksal modelin doğruluğunu kanıtlamaktadır.

## 🚀 Kurulum ve Çalıştırma
Proje Java diliyle geliştirilmiştir. Çalıştırmak için:

1. Depoyu klonlayın:
   ```bash
   git clone [https://github.com/kullaniciadi/repo-adi.git](https://github.com/kullaniciadi/repo-adi.git)
