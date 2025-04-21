import matplotlib
import matplotlib.pyplot as plt

matplotlib.rcParams['font.sans-serif'] = ['SimHei']
matplotlib.rcParams['axes.unicode_minus'] = False

values = [42651, 44412, 45405, 45101, 45400, 45520, 44927, 44931]

labels = ['400', '600', '800', '1000', '1200',
          '1400', '1600', '1800']

plt.figure(figsize=(8, 5))
plt.bar(labels, values, color='skyblue', edgecolor='black')

plt.title('批量导入用时比较')
plt.xlabel('batch size')
plt.ylabel('records/s')

# 显示图形
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()
plt.show()
