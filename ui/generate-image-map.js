const imageFolder = './src/assets/images/header-studienfeld/';
const targetFile = './src/app/main/studienfeldinfo/headerimagemap.ts';
const fs = require('fs');



fs.readdir(imageFolder, (err, files) => {

  var map = {};

  files.forEach(file => {

    var regex = /(\d+)/g;

    var ids = file.match(regex);
    if (ids === null || ids.length == 0) {
      throw 'Der Imagename ' + file + ' enthaelt keine ID!';
    }

    //console.log(ids);

    for (i = 0; i < ids.length; i++) {
      map[ids[i]] = file;
    }
  });

  var json = JSON.stringify(map, null, 4);

  var warning = '// ACHTUNG: Dieses File wird während des Bau-Prozesses immer neu generiert (Skript: generate-image-map.js)!';
  var disableLint = '// tslint:disable:object-literal-sort-keys';
  var contents = warning + '\n' + disableLint + '\n\nexport const HEADER_IMAGE_MAP = ' + json + ';\n';

  fs.writeFile(targetFile, contents, 'utf8', ()=> {});
});
