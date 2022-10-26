#!/bin/sh

source /etc/profile

[ -z "$SPARROW_CODER_HOME" ] && echo "please config environment variable SPARROW_CODER_HOME" && exit 0

# Environment Variable Prerequisites
# sh coder.sh -ct com.sparrow.file.po.Attach
# sh coder.sh -ct com.sparrow.file.po.AttachRef
#


class_path=$(cd ../target/classes;pwd)
sparrow_coder_name=sparrow-coder-all.jar

[ -n "$SPARROW_CODER" ] && sparrow_coder_name=$SPARROW_CODER

echo java -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.Main $@

java -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.Main $@



