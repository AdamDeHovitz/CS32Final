bulkAppControllers.controller("eventCtrl", 
	function($scope, $http, $rootScope, $stateParams, $ionicPopup, $state) {

	var eventId = $stateParams.eventId;
	console.log(eventId);
	
	 // A confirm dialog
	 $scope.showConfirm = function() {
	   var confirmPopup = $ionicPopup.confirm({
	     title: 'Confirm Join',
	     template: 'Are you sure you want to join this event?'
	   });

	   confirmPopup.then(function(res) {
	     if(res) {
	       console.log('You are sure');
	     } else {
	       console.log('You are not sure');
	     }
	   });
	 };
	 
	// get event info from server
	$scope.event = {
			title: "Watermelon",
			eventId: 1,
			author: "Barack Obama",
			authorId: "abc123",
			authorImg: 'http://a5.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE4MDAzNDEwNzg5ODI4MTEw.jpg',
			img: "http://cdn.skim.gs/image/upload/v1456343706/msi/isolated-slice-of-watermelon_w6khuv.jpg",
			location: "Costco",
			description: "come buy lit watermelons",
			price: 2,
			curMemberNum: 2,
			desiredMembers: 5,
			tags: ["watermelon", "produce", "fresh", "fruit"]
		};
	
	 $scope.goAuthor = function() {
		 $state.go('tab.profile',{userId: $scope.event.authorId});
	 }

		/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
			})
		});
		$http.post("/feedEntries", postData).success(function(data, status) {
			$scope.events = data.entries;
		});*/
});