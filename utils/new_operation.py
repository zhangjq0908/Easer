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
            'plugin': templates.tmpl_operation_plugin,
            'data': templates.tmpl_operation_data,
            'data_factory': templates.tmpl_operation_data_factory,
            'view_fragment': templates.tmpl_plugin_view_fragment,
            'loader': templates.tmpl_operation_loader,
            },
        'androidTest': {
            'androidTest$data': templates.tmpl_androidTest_data,
            },
        }

def new_operation(cname, identifier):
    pdef = {}
    pdef['package'] = "skills.operation.{}".format(identifier)
    pdef['plugin'] = "{}OperationPlugin".format(cname)
    pdef['id'] = identifier
    pdef['data'] = "{}OperationData".format(cname)
    pdef['data_factory'] = "{}OperationDataFactory".format(cname)
    pdef['view_fragment'] = "{}PluginViewFragment".format(cname)
    pdef['loader'] = "{}Loader".format(cname)
    pdef['androidTest$data'] = "{}OperationDataTest".format(cname)
    for t, template_map in template_maps.items():
        dest = "app/src/{}/java/ryey/easer/plugins/operation/{}".format(t, identifier)
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
    new_operation(args.cname, args.id)

