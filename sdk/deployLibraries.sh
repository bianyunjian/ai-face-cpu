#apt source
mv /etc/apt/sources.list /etc/apt/sources.list.bak
echo '' > /etc/apt/sources.list

echo 'deb http://mirrors.163.com/debian/ jessie main non-free contrib' >>  /etc/apt/sources.list
echo 'deb http://mirrors.163.com/debian/ jessie-updates main non-free contrib' >>  /etc/apt/sources.list
echo 'deb-src http://mirrors.163.com/debian/ jessie main non-free contrib' >>  /etc/apt/sources.list
echo 'deb-src http://mirrors.163.com/debian/ jessie-updates main non-free contrib' >>  /etc/apt/sources.list
echo 'deb http://mirrors.163.com/debian-security/ jessie/updates main non-free contrib' >>  /etc/apt/sources.list
echo 'deb-src http://mirrors.163.com/debian-security/ jessie/updates main non-free contrib ' >>  /etc/apt/sources.list
echo 'deb http://mirrors.aliyun.com/ubuntu/ xenial main restricted universe multiverse' >>  /etc/apt/sources.list
echo 'deb http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted universe multiverse' >>  /etc/apt/sources.list
echo 'deb http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted universe multiverse' >>  /etc/apt/sources.list
echo 'deb http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse' >>  /etc/apt/sources.list
##测试版源
echo 'deb http://mirrors.aliyun.com/ubuntu/ xenial-proposed main restricted universe multiverse' >>  /etc/apt/sources.list
# 源码
echo 'deb-src http://mirrors.aliyun.com/ubuntu/ xenial main restricted universe multiverse' >>  /etc/apt/sources.list
echo 'deb-src http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted universe multiverse' >>  /etc/apt/sources.list
echo 'deb-src http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted universe multiverse' >>  /etc/apt/sources.list
echo 'deb-src http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse' >>  /etc/apt/sources.list
##测试版源
echo 'deb-src http://mirrors.aliyun.com/ubuntu/ xenial-proposed main restricted universe multiverse' >>  /etc/apt/sources.list
# Canonical 合作伙伴和附加
echo 'deb http://archive.canonical.com/ubuntu/ xenial partner' >>  /etc/apt/sources.list
echo 'deb http://security.ubuntu.com/ubuntu/ bionic-security universe multiverse restricted main' >>  /etc/apt/sources.list
echo 'deb http://mirrors.aliyun.com/ubuntu/ bionic-updates universe multiverse restricted main' >>  /etc/apt/sources.list
echo 'deb http://mirrors.aliyun.com/ubuntu/ bionic universe main restricted multiverse' >>  /etc/apt/sources.list

apt-key adv --keyserver keyserver.ubuntu.com --recv-keys AA8E81B4331F7F50
apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 3B4FE6ACC0B21F32
apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 40976EAF437D05B5

cat /etc/apt/sources.list
apt update

#apt install packages
apt -y install  vim
apt -y install  libgomp1
apt -y install  libilmbase-dev
apt -y install  libopenblas-dev
apt -y install  libilmbase6
apt -y install  libopenexr-dev
apt clean
#copy libs
echo '/sdk' > /etc/ld.so.conf.d/facesdk.conf
#cp /sdk/*.so /usr/local/lib -rf
#cp /sdk/*.so.* /usr/local/lib -rf

ldconfig

ldd /sdk/libarSDK4CommonBundle.so
