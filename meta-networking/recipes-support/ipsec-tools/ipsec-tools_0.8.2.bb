DESCRIPTION = "IPsec-Tools is a port of KAME's IPsec utilities to the \
Linux-2.6 IPsec implementation."
HOMEPAGE = "http://ipsec-tools.sourceforge.net/"
SECTION = "console/network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://src/libipsec/pfkey.c;beginline=6;endline=31;md5=bc9b7ff40beff19fe6bc6aef26bd2b24"

DEPENDS = "virtual/kernel openssl readline flex-native bison-native"

SRC_URI = "ftp://ftp.netbsd.org/pub/NetBSD/misc/ipsec-tools/0.8/ipsec-tools-${PV}.tar.bz2 \
           file://0002-Don-t-link-against-libfl.patch \
           file://configure.patch \
           file://0001-racoon-pfkey-avoid-potential-null-pointer-dereferenc.patch \
           file://racoon-check-invalid-pointers.patch \
           file://racoon-check-invalid-ivm.patch \
          "
SRC_URI[md5sum] = "d53ec14a0a3ece64e09e5e34b3350b41"
SRC_URI[sha256sum] = "8eb6b38716e2f3a8a72f1f549c9444c2bc28d52c9536792690564c74fe722f2d"

inherit autotools

# Options:
#  --enable-adminport      enable admin port
#  --enable-rc5            enable RC5 encryption (patented)
#  --enable-idea enable IDEA encryption (patented)
#  --enable-gssapi         enable GSS-API authentication
#  --enable-hybrid         enable hybrid, both mode-cfg and xauth support
#  --enable-frag           enable IKE fragmentation payload support
#  --enable-stats          enable statistics logging function
#  --enable-dpd            enable dead peer detection
#  --enable-samode-unspec  enable to use unspecified a mode of SA
#  --disable-ipv6          disable ipv6 support
#  --enable-natt           enable NAT-Traversal (yes/no/kernel)
#  --enable-natt-versions=list    list of supported NAT-T versions delimited by coma.
#  --with-kernel-headers=/lib/modules/<uname>/build/include
#                          where your Linux Kernel headers are installed
#  --with-readline         support readline input (yes by default)
#  --with-flex             use directiory (default: no)
#  --with-flexlib=<LIB>    specify flex library.
#  --with-openssl=DIR      specify OpenSSL directory
#  --with-libradius=DIR    specify libradius path (like/usr/pkg)
#  --with-libpam=DIR       specify libpam path (like/usr/pkg)
#
# Note: if you give it the actual kernel headers it won't build, it actually
# needs to point at the linux-libc-headers version of the kernel headers.
#
EXTRA_OECONF = "--with-kernel-headers=${STAGING_INCDIR} \
                --with-readline \
                --with-openssl=${STAGING_LIBDIR}/.. \
                --without-libradius \
                --disable-security-context \
                --enable-shared \
                --enable-dpd \
                --enable-natt=yes \
                ${@base_contains('DISTRO_FEATURES', 'ipv6', '--enable-ipv6=yes', '', d)}"

# See http://bugs.debian.org/cgi-bin/bugreport.cgi?bug=530527
CFLAGS += "-fno-strict-aliasing"

PACKAGECONFIG ??= ""
PACKAGECONFIG[pam] = "--with-libpam,--without-libpam,libpam,"
PACKAGECONFIG[selinux] = "--enable-security-context,--disable-security-context,libselinux,"
