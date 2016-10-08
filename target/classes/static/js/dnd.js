/**
 * Created by Артем Константинович on 06.10.2016.
 */
//noinspection JSAnnotator
/**
 * The controller doesn't do much more than setting the initial data model
 */
angular.module("dnd", ['dndLists','ngSanitize','summernote'])

    .controller("editCtrl", function($scope, $http) {
        $scope.models = {
            selected: null,
            templates: [
                {type: "item", code: ""},
                {type: "container", columns: [[], []]}
            ],
            dropzones: {
                "A": []
            }
        };

        $scope.$watch('models.dropzones', function (model) {
            $scope.modelAsJson = angular.toJson(model, true);
        }, true);

        $scope.savePage = function (action) {
            console.log($scope.modelAsJson);
            var source = $scope.modelAsJson;
            $http.post("edit/source", source, action).success(function (selectedDropZone) {

                $scope.models.dropzones.Body = $scope.modelAsJson.Body;
            })};

    }
);