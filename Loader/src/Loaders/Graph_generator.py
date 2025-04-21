import matplotlib
import matplotlib.pyplot as plt

matplotlib.rcParams['font.sans-serif'] = ['SimHei']
matplotlib.rcParams['axes.unicode_minus'] = False

values = [18, 3636, 5446, 13160, 44029, 99482, 202922]

labels = ['Awful', 'Connection', 'Prepare', 'Transaction', 'Batch',
          'Batch no trigger', 'Multi-threads no trigger']

plt.figure(figsize=(8, 5))
plt.bar(labels, values, color='skyblue', edgecolor='black')

plt.title('导入用时比较')
plt.xlabel('导入方式')
plt.ylabel('条数/秒')

# 显示图形
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()
plt.show()
