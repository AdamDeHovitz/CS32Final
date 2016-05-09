bulkAppControllers.controller("mapCtrl", 
	function($scope, $rootScope, $http, $ionicLoading, $stateParams, $state, $timeout, $interval) {
    console.log("here i am");
	var eventId = $stateParams.eventId;
	$.post("/event-view", {id : eventId, userId : $rootScope.account.id}, function(responseJSON) {
    			responseObject = JSON.parse(responseJSON);
    			$scope.event = responseObject;
    			console.log(event);
    			var isOwner = responseObject.authorId == $rootScope.account.id;
    			initMap();
    			});

    function getLoc(callback) {
                if (navigator.geolocation) {
                  var startPos;
                  var geoOptions = {
                     maximumAge:  2 * 1000,
                     timeout: 15 * 1000
                  }
                  var geoSuccess = function(position) {
                    startPos = position;
                    $rootScope.lat = startPos.coords.latitude;
                    $rootScope.lng = startPos.coords.longitude;

                    console.log("retrieved location");
                    $scope.yourLoc = new google.maps.LatLng($rootScope.lat,
                    $rootScope.lng);
                    callback();
                  };
                  var geoError = function(error) {
                    console.log('Error occurred. Error code: ' + error.code);
                    callback();
                  };

                  navigator.geolocation.getCurrentPosition(geoSuccess, geoError, geoOptions);

                }
                else {
                    console.log('Geolocation is not supported for this Browser/OS version yet.');
                    callback();
                }
               }

    function initMap() {
    var eventLoc = new google.maps.LatLng($scope.event.lat, $scope.event.lng);

    var mapOptions = {
                center: eventLoc,
                zoom: 20,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };

            var map = new google.maps.Map(document.getElementById("map"), mapOptions);

            var marker = new google.maps.Marker({
                position: eventLoc,
                map: map,
                title: 'Event'
              });
            function addLoc() {
                var urmarker = new google.maps.Marker({
                            position: $scope.yourLoc,
                            map: map,
                            icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png',
                            title: 'You'
                          });
                var bounds = new google.maps.LatLngBounds();
                bounds.extend(eventLoc);
                bounds.extend($scope.yourLoc);
                map.fitBounds(bounds);
            }
            getLoc(addLoc);
/*
            navigator.geolocation.getCurrentPosition(function(pos) {
                map.setCenter(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
                var myLocation = new google.maps.Marker({
                    position: new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude),
                    map: map,
                    title: "My Location"
                });
            });
*/
            $scope.map = map;
            }


});

