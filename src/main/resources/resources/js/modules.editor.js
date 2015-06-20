mindcloud.modules.editor = {};
(function (editor) {
    var editorPanel;
    var menuPanel;

    editor.init = function () {
        editorPanel = $('.mindmap-editor');
        menuPanel = editorPanel.find('.navbar-nav');
        menuPanel.find('> li > [data-toggle="dropdown"]').hover(function (event) {
            var dropdowns = menuPanel.find('> li > [data-toggle="dropdown"]');
            var open = false;
            for (var i = 0; i < dropdowns.length; i++) {
                var dropdown = $(dropdowns[i]);
                if (dropdown.parent().hasClass('open')) {
                    dropdown.dropdown('toggle');
                    open = true;
                }
            }
            if (open) {
                $(this).dropdown('toggle');
            }
        });
    };
})(mindcloud.modules.editor);