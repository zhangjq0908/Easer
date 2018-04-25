#!/usr/bin/env python3
# -*- coding:utf-8 -*-
#
#   Author  :   renyuneyun
#   E-mail  :   renyuneyun@gmail.com
#   Date    :   18/04/22 00:15:11
#   License :   Apache 2.0 (See LICENSE)
#

'''

'''

import argparse
import sys
import os.path
import templates

template_maps = {
        'main': {
            'plugin': templates.tmpl_event_plugin,
            'data': templates.tmpl_event_data,
            'data_factory': templates.tmpl_event_data_factory,
            'view_fragment': templates.tmpl_plugin_view_fragment,
            'slot': templates.tmpl_event_slot,
            },
        'androidTest': {
            'androidTest$data': templates.tmpl_androidTest_data,
            },
        }

def new_event(cname, identifier):
    pdef = {}
    pdef['package'] = "ryey.easer.plugins.event.{}".format(identifier)
    pdef['plugin'] = "{}EventPlugin".format(cname)
    pdef['id'] = identifier
    pdef['data'] = "{}EventData".format(cname)
    pdef['data_factory'] = "{}EventDataFactory".format(cname)
    pdef['view_fragment'] = "{}PluginViewFragment".format(cname)
    pdef['slot'] = "{}Slot".format(cname)
    pdef['androidTest$data'] = "{}EventDataTest".format(cname)
    for t, template_map in template_maps.items():
        dest = "app/src/{}/java/ryey/easer/plugins/event/{}".format(t, identifier)
        if not os.path.isfile(dest):
            os.mkdir(dest)
        for k in template_map:
            class_content = template_map[k].format_map(pdef)
            with open("{}/{}.java".format(dest, pdef[k]), 'w') as fd:
                fd.write(templates.tmpl_copyright)
                fd.write('\n')
                fd.write(class_content)

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('cname', metavar='class_name_prefix', help='Prefix for all classes of this plugin')
    parser.add_argument('id', help='Internal unique identifier of this plugin. Also used as the package name')
    args = parser.parse_args()
    new_event(args.cname, args.id)

