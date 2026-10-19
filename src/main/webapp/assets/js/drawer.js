/**
 * drawer.js - Điều khiển đóng mở Side Panel (Drawer Menu)
 */
function openDrawer() {
    var drawer = document.getElementById('drawerMenu');
    var overlay = document.getElementById('drawerOverlay');
    if (drawer) drawer.className = 'drawer-menu active';
    if (overlay) overlay.className = 'drawer-overlay active';
}

function closeDrawer() {
    var drawer = document.getElementById('drawerMenu');
    var overlay = document.getElementById('drawerOverlay');
    if (drawer) drawer.className = 'drawer-menu';
    if (overlay) overlay.className = 'drawer-overlay';
}
